package com.springboot.app.security.config;


import com.springboot.app.security.jwt.AuthEntryPointJwt;
import com.springboot.app.security.jwt.AuthTokenFilter;
import com.springboot.app.security.oauth2.CustomOAuth2UserService;
import com.springboot.app.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.springboot.app.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.springboot.app.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    public WebSecurityConfig(@Lazy UserDetailsService userDetailsService, AuthEntryPointJwt unauthorizedHandler, @Lazy CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/", "/error", "/api/auth/**").permitAll()
                                .requestMatchers("/api/reset-password/**").permitAll()
                                .requestMatchers("/api/user-stat/**", "/api/view/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/discussions/*", "/api/discussions/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/comments/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/banned-keywords/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/account-info/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/mobile/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/mobile/follow/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/user-history/comments").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/follows/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/discussions/*", "/api/discussions/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/comments/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/banned-keywords/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/account-info/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/mobile/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/mobile/follow/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/user-history/comments").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/follows/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                // by role
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                // by role
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                // OpenAPI endpoints
                                .requestMatchers("/v3/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/").deleteCookies("JSESSIONID", "SESSION", "jwt-refresh")
                );

        http.authenticationProvider(authenticationProvider());

        http.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                .redirectionEndpoint(redirection -> redirection
                        .baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler));
        http.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                .redirectionEndpoint(redirection -> redirection.baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler).failureHandler(oAuth2AuthenticationFailureHandler));

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors(cfg -> new CorsConfiguration().applyPermitDefaultValues());

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("http://127.0.0.1:8080", "http://10.0.2.2:8080") // Add Flutter's localhost
                        // add ReactJS's localhost
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "http://localhost:5174")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies or authorization headers
            }
        };
    }
}
