package com.gucardev.springsecurityexamples.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private final PasswordEncoder passwordEncoder;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
        web.ignoring()
            .requestMatchers(
                new AntPathRequestMatcher("/auth/**"),
                new AntPathRequestMatcher("/public/**"));
  }

  @Bean
  public UserDetailsService users() {
    UserDetails user =
        User.builder()
            .username("user")
            .password("pass")
            .passwordEncoder(passwordEncoder::encode)
            // .password(passwordEncoder.encode("pass"))
            .roles("USER")
            .build();
    UserDetails admin =
        User.builder()
            .username("admin")
            .password(passwordEncoder.encode("pass"))
            .roles("USER", "ADMIN")
            .build();
    UserDetails mod =
        User.builder()
            .username("mod")
            .password(passwordEncoder.encode("pass"))
            .roles("MOD")
            .build();
    return new InMemoryUserDetailsManager(user, admin, mod);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(x -> x.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return httpSecurity.build();
  }
}
