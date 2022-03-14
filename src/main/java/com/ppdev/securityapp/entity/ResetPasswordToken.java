package com.ppdev.securityapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("reset")
public class ResetPasswordToken extends BaseToken{
    public ResetPasswordToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             User user){
        super(token, createdAt, expiresAt, user);
    }

}
