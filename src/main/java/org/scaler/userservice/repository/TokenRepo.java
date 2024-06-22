package org.scaler.userservice.repository;

import org.scaler.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TokenRepo extends JpaRepository<Token, Long> {

    //@Override
    Optional<Token> findByValue(String token);
}
