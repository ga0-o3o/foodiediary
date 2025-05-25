package foodiediary.user.dto;
import foodiediary.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserFormDto {
    public UserFormDto(){}

    private String name;
    private String id;
    private String pw;
    private String phoneNum;

    public User getUserEntity(){
        User user = new User();
        user.setId(id);
        user.setPw(pw);
        user.setName(name);
        user.setPhoneNum(phoneNum);

        return user;
    }

    public UserFormDto change2UserFormDto(User user){
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.name = user.getName();
        userFormDto.id = user.getId();
        userFormDto.pw = user.getPw();
        userFormDto.phoneNum = user.getPhoneNum();

        return userFormDto;
    }

}
