package foodiediary.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserFormDto {
    public UserFormDto(){}

    public UserFormDto(String name, String id, String pw, String phoneNum){
        this.name = name;
        this.id = id;
        this.pw =pw;
        this.phoneNum=phoneNum;
    }
    private String name;
    private String id;
    private String pw;
    private String phoneNum;

}
