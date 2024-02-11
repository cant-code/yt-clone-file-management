package com.cantcode.yt.filemanagement.webapp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.FILE_MANAGEMENT_BASE_URL;
import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.VIDEO;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new RegexRequestMatcher(FILE_MANAGEMENT_BASE_URL + VIDEO + "/\\d+/stream", HttpMethod.GET.name())).permitAll()
                        .requestMatchers("/api/file-management/videos", HttpMethod.GET.name()).permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
