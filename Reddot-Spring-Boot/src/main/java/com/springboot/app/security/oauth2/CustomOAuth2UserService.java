package com.springboot.app.security.oauth2;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.enumeration.AccountStatus;
import com.springboot.app.accounts.enumeration.AuthProvider;
import com.springboot.app.accounts.repository.RoleRepository;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.accounts.service.UserService;
import com.springboot.app.security.exception.OAuth2AuthenticationProcessingException;
import com.springboot.app.security.oauth2.user.OAuth2UserInfo;
import com.springboot.app.security.oauth2.user.OAuth2UserInfoFactory;
import com.springboot.app.security.service.GitHubService;
import com.springboot.app.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GitHubService gitHubService;
    private final UserService userService;

    private static boolean isProviderEquals(OAuth2UserRequest oAuth2UserRequest, String provider) {
        return oAuth2UserRequest.getClientRegistration().getRegistrationId().equals(provider);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        String email;
        if (oAuth2UserInfo.getEmail() == null && isProviderEquals(oAuth2UserRequest, "github")) {
            // call GitHubService to get emails
            CompletableFuture<List<GitHubService.GitHubEmail>> futureEmails = CompletableFuture.supplyAsync(() ->
                    gitHubService.fetchEmails(oAuth2UserRequest.getAccessToken().getTokenValue(), true)
            );
            try {
                List<GitHubService.GitHubEmail> primaryEmailObject = futureEmails.get();
                email = primaryEmailObject.getFirst().getEmail();
            } catch (ExecutionException | InterruptedException e) {
                throw new OAuth2AuthenticationProcessingException("Failed to get email from GitHub");
            }
        } else if (oAuth2UserInfo.getEmail() != null) {
            email = oAuth2UserInfo.getEmail();
        } else {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider or you do not set email visibility to public.");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = new User();
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                                                                  user.getProvider() + " account. Please use your " + user.getProvider() +
                                                                  " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user.setUsername(createUsername());
            user.setEmail(email);
            user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
            user.setProviderId(oAuth2UserInfo.getId());
            user.setName(oAuth2UserInfo.getName());
            user.setImageUrl(oAuth2UserInfo.getImageUrl());
            user.setAccountStatus(AccountStatus.ACTIVE);
            user.setCreatedBy(user.getUsername());
            boolean result = userService.createOAuthUser(user);
            if (!result) {
                throw new OAuth2AuthenticationProcessingException("Failed to create user");
            }
        }
        return UserDetailsImpl.create(user, oAuth2User.getAttributes());
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        existingUser.setUpdatedBy(existingUser.getUsername());
        return userRepository.save(existingUser);
    }

    private String createUsername() {
        String username = null;
        while (username == null) {
            username = "user" + (int) (Math.random() * 10000);
            if (userRepository.findByUsername(username).isPresent()) {
                username = null;
            }
        }
        return username;
    }
}
