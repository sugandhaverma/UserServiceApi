package org.scaler.userservice.dtos;

import lombok.Data;

@Data
public class LoginDtoRequest {
    private String email;
    private String password;
}
