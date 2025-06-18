package foodiediary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResultDto {
    private boolean success;
    private String message;
}
