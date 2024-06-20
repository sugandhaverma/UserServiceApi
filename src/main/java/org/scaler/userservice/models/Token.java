package org.scaler.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Token extends BaseModel{
    private Date expiry;
    private boolean is_Valid;
    private String value;

    @ManyToOne
    private User user;

}
