package com.gucardev.springsecurityexamples.repository;

import com.gucardev.springsecurityexamples.model.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findTokenByTokenAndValidTrue(String token);

  Optional<Token> findTokenByTokenAndValidFalse(String token);
}
