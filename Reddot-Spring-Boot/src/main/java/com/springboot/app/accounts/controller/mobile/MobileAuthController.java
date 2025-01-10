package com.springboot.app.accounts.controller.mobile;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.RoleRepository;
import com.springboot.app.accounts.service.UserService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.security.dto.request.LoginRequest;
import com.springboot.app.security.dto.request.SignupRequest;
import com.springboot.app.security.dto.response.JwtResponse;
import com.springboot.app.security.entity.RefreshToken;
import com.springboot.app.security.exception.TokenRefreshException;
import com.springboot.app.security.jwt.JwtUtils;
import com.springboot.app.security.service.RefreshTokenService;
import com.springboot.app.security.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mobile/auth")
public class MobileAuthController {
    private static final Logger logger = LoggerFactory.getLogger(MobileAuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ObjectResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        ServiceResponse<User> response = userService.createNewUser(signUpRequest);
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            String errorMessage = String.join(", ", response.getMessages());
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "User not created. " + errorMessage, null));
        }
        return ResponseEntity.ok(new ObjectResponse("201", "User created", response.getDataObject()));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
        String avatar = refreshToken.getUser().getAvatar();
        String imageUrl = refreshToken.getUser().getImageUrl();
        String name = refreshToken.getUser().getName();

        userService.updateLastLogin(userDetails.getId()); // update last login

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new JwtResponse(jwtCookie.getValue(), userDetails.getId(), userDetails.getUsername(),
                        userDetails.getEmail(), roles, avatar, imageUrl, name));
    }

    @PostMapping("/signout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        var sessionUser = JwtUtils.getSession();
        logger.info("User logout: {}", sessionUser.getId());

        ServiceResponse<Void> response = refreshTokenService.deleteByToken(refreshToken, sessionUser.getId());
        if (response.getAckCode() != AckCodeType.SUCCESS) {
            String errorMessage = String.join(", ", response.getMessages());
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "User not logged out. " + errorMessage, null));
        }
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new ObjectResponse("200", sessionUser.getUsername() + " logged out successfully!", null));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<ObjectResponse> refreshtoken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
//		log("refreshToken: " + refreshToken);
        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser).map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
                        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(newRefreshToken.getToken());
                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                                .body(new ObjectResponse("200", "Token is refreshed successfully!", jwtCookie.getValue()));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }
        return ResponseEntity.badRequest().body(new ObjectResponse("400", "Refresh Token is empty!", null));
    }


}
