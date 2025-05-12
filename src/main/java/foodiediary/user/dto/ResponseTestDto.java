package foodiediary.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ResponseTestDto {
    public ResponseTestDto(){}
    public ResponseTestDto(String name, String id, String pw, String phoneNum){
        this.name = name;
        this.id=id;
        this.pw=pw;
        this.phoneNum=phoneNum;
    }
    private String name;
    private String id;
    private String pw;
    private String phoneNum;
}
