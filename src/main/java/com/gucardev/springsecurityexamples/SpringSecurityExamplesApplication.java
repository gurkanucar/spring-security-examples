package com.gucardev.springsecurityexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
public class SpringSecurityExamplesApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringSecurityExamplesApplication.class, args);
  }
}
