package ru.java.mentor.oldranger.club.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordRecoveryDto {

    private String token;
    private String tokenStatus;
    private String passwordStatus;
}
