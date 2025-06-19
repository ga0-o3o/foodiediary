package foodiediary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchResponseDto {
    private String id;
    private String name;
    private String phoneNum;
}