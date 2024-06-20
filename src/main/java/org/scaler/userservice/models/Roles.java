package org.scaler.userservice.models;


import jakarta.persistence.Entity;
import jdk.jfr.Enabled;
import lombok.Data;

@Entity
@Data
public class Roles extends BaseModel {
    private String role;
}
