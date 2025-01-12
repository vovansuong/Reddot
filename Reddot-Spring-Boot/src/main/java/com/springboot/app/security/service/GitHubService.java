package com.springboot.app.security.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GitHubService {
    private final WebClient webClient;

    public GitHubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public List<GitHubEmail> fetchEmails(String accessToken, boolean onlyPrimary) {
        List<GitHubEmail> emails = webClient.get()
                .uri("/user/emails")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToFlux(GitHubEmail.class)
                .collectList()
                .block();

        if (onlyPrimary && emails != null) {
            return emails.stream()
                    .filter(GitHubEmail::isPrimary)
                    .toList();
        } else {
            return emails;
        }
    }

    @Setter
    @Getter
    public static class GitHubEmail {
        // Getters and setters
        private String email;
        private boolean primary;
        private boolean verified;
        private String visibility;
    }
}