package org.scaler.userservice.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.scaler.userservice.dtos.UserDto;
import org.scaler.userservice.models.Token;
import org.scaler.userservice.models.User;
import org.scaler.userservice.repository.TokenRepo;
import org.scaler.userservice.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepo userRepo;

    private TokenRepo tokenRepo;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder,UserRepo userRepo, TokenRepo tokenRepo) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
    }
    public User signUp(String email, String password, String name) {
        User u = new User();
        u.setEmail(email);
        u.setVerified(false);
        u.setName(name);
        u.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepo.save(u);
    }
    public Token login(String email, String password) {
        // 1 find user by email
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found");
        }

        User u = userOptional.get();

        // 2 match password with matches
        if(!bCryptPasswordEncoder.matches(password, u.getHashedPassword())){
            throw new RuntimeException("Wrong password");
        }
        // 3 generate token

        Token t = generateToken(u);

        // 4 save token
         t =    tokenRepo.save(t);

        // 5 return token
        return t;
    }
    private Token generateToken(User u){
        LocalDate currentDate = LocalDate.now();
        LocalDate ThirtyDateLater = currentDate.plusDays(30);

        Date expiry = Date.from(ThirtyDateLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Token token = new Token();
        token.setExpiry(expiry);
        token.setUser(u);
        token.setValid(true);

        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        return token;

    }
    public void Logout(String token){
        Optional <Token> optionalToken =    tokenRepo.findByValue(token);
        if(optionalToken.isEmpty()){
      //throw runtimeException
            return;
        }
        Token t = optionalToken.get();
        t.setValid(false);
        tokenRepo.save(t);
    }

    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepo.findByValueAndIsValidAndExpiryGreaterThan(token, true, new Date());

        if (optionalToken.isEmpty()) {
            throw  new RuntimeException("Token not found");
           // return null;
        }

        User u =  optionalToken.get().getUser();
      return  u;
    }

}
