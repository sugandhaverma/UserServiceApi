package org.scaler.userservice.dtos;

import lombok.Data;

@Data
public class LoginDtoResponse {
    String tokenValue;
    String status;

}
