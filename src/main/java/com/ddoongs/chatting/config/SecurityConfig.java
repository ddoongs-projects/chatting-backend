package com.ddoongs.chatting.config;

import com.ddoongs.chatting.auth.RestApiLoginAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      AuthenticationManager authenticationManager) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable);
    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);

    RestApiLoginAuthFilter restApiLoginAuthFilter = new RestApiLoginAuthFilter(
        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/auth/login"),
        authenticationManager);

    http.addFilterAt(restApiLoginAuthFilter, UsernamePasswordAuthenticationFilter.class);

    http.authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
        .requestMatchers("/ws/v1/chat").permitAll()
        .anyRequest()
        .authenticated());

    http.logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
        .logoutSuccessHandler(this::logoutHandler));

    return http.build();
  }

  private void logoutHandler(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
    response.setCharacterEncoding("UTF-8");

    String message;

    if (authentication != null && authentication.isAuthenticated()) {
      message = "로그아웃 성공";
    } else {
      message = "로그아웃 실패";
    }

    try {
      response.getWriter().write(message);
    } catch (IOException ex) {
      log.error("전송 실패. 원인: {}", ex.getMessage());
    }
  }

}
