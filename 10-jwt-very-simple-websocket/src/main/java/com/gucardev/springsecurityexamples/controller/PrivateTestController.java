package com.gucardev.springsecurityexamples.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateTestController {

  @GetMapping
  public ResponseEntity<String> sayHelloToAnyAuthenticated() {
    return ResponseEntity.ok("Hello Any Authenticated! This is private endpoint");
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<String> sayHelloToAdmin() {
    return ResponseEntity.ok("Hello Admin! This is private endpoint");
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
  @GetMapping("/user")
  public ResponseEntity<String> sayHelloToUser() {
    return ResponseEntity.ok("Hello User! This is private endpoint");
  }
}
