package com.springboot.app.accounts.controller;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.service.UserService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.security.dto.CurrentUser;
import com.springboot.app.security.dto.request.LoginRequest;
import com.springboot.app.security.dto.request.SignupRequest;
import com.springboot.app.security.dto.response.JwtResponse;
import com.springboot.app.security.dto.response.UserInfoResponse;
import com.springboot.app.security.entity.RefreshToken;
import com.springboot.app.security.exception.TokenRefreshException;
import com.springboot.app.security.jwt.JwtUtils;
import com.springboot.app.security.service.RefreshTokenService;
import com.springboot.app.security.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public static ResponseEntity<ObjectResponse> getObjectResponseResponseEntity(HttpServletRequest request, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
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

    /**
     * This method will register a new user in the application and return a success
     * message if the user is registered successfully.
     */
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
        try {
            UserInfoResponse sessionUser;
            ResponseCookie jwtRefreshCookie;
            String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

            sessionUser = JwtUtils.getSession();
            log.info("User logout: {}", sessionUser.getId());

            refreshTokenService.deleteByToken(refreshToken, sessionUser.getId());
            jwtUtils.getCleanJwtCookie();
            jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(new ObjectResponse("200", "User logged out successfully!", null));
        } catch (Exception e) {
            // sign out even if the exception is thrown
            SecurityContextHolder.clearContext();
            return ResponseEntity.badRequest().body(new ObjectResponse("400", "User not logged out!", null));
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<ObjectResponse> refreshToken(HttpServletRequest request) {
        return getObjectResponseResponseEntity(request, jwtUtils, refreshTokenService);
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserDetailsImpl userDetails) {

        Set<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
        String avatar = refreshToken.getUser().getAvatar();
        String imageUrl = refreshToken.getUser().getImageUrl();
        String name = refreshToken.getUser().getName();
        log.info("Refresh token: {}", jwtRefreshCookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new JwtResponse(jwtCookie.getValue(), userDetails.getId(), userDetails.getUsername(),
                        userDetails.getEmail(), roles, avatar, imageUrl, name));
    }
}
