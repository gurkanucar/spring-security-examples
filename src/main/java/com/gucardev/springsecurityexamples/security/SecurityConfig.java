package com.gucardev.springsecurityexamples.security;

import com.gucardev.springsecurityexamples.security.oauth.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Bean
  public AuthenticationManager authenticationManager(
      final AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
        web.ignoring()
            .requestMatchers(
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/auth/**"),
                new AntPathRequestMatcher("/public/**"),
                new AntPathRequestMatcher("/h2-console/**"));
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
    httpSecurity
        .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .exceptionHandling(
            handlingConfigurer -> {
              handlingConfigurer.accessDeniedHandler(jwtAccessDeniedHandler);
              // comment it if you want to use form login
              //  handlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
            })
        .authorizeHttpRequests(
            x ->
                x
                    // .requestMatchers(new MvcRequestMatcher(introspector, "/login")) .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
        .oauth2Login(
            x -> {
              x.defaultSuccessUrl("/oauth2/redirectCustom");
              x.successHandler(customAuthenticationSuccessHandler);

            })
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }
}
