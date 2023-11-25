package com.gucardev.springsecurityexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringSecurityExamplesApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringSecurityExamplesApplication.class, args);
  }
}
