package org.scaler.userservice.controller;


import org.scaler.userservice.dtos.*;
import org.scaler.userservice.models.Token;
import org.scaler.userservice.models.User;
import org.scaler.userservice.repository.TokenRepo;
import org.scaler.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final TokenRepo tokenRepo;
    private UserService userService;

    UserController(UserService userService, TokenRepo tokenRepo) {
        this.userService = userService;
        this.tokenRepo = tokenRepo;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequest requestDto) {
        User u = userService.signUp(
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getPassword()
        );

        return UserDto.from(u);
    }

    @PostMapping("/login")
    public LoginDtoResponse login(@RequestBody LoginDtoRequest loginDtoRequest) {

        Token t = userService.login(loginDtoRequest.getEmail(), loginDtoRequest.getPassword());
        LoginDtoResponse response = new LoginDtoResponse();

        if (t != null) {

            response.setTokenValue(t.getValue());
            response.setStatus("Success");
            return response;
        }
        response.setStatus("Failed");
        return response;

    }
@PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutDtoRequest logoutDtoRequest) {
        try {
            userService.Logout(logoutDtoRequest.getToken());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
        User u = userService.validateToken(token);

        return UserDto.from(u);
    }

}




