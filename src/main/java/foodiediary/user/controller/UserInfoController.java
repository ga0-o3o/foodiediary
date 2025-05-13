package foodiediary.user.controller;

import foodiediary.user.dto.ResponseTestDto;
import foodiediary.user.dto.UserFormDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class UserInfoController {

    @GetMapping("/foodiediary/user/info")
    public ResponseEntity<ResponseTestDto> getUserInfo(HttpServletRequest request){
        ResponseTestDto res = new ResponseTestDto("gayeong","gayeong1204","qwert123!","23089268");
        return ResponseEntity.ok(res);
    }
    @PatchMapping("/foodiediary/user/info")
    public ResponseEntity<Void> editUserInfo(HttpServletRequest request, @RequestBody UserFormDto editDto){
        //해당 사용자 불러옴 >> id로 검색하기

        //서비스로직으로 묶어버리기?
        String id = (String) request.getAttribute("id");
        UserFormDto originDto = new UserFormDto("123", "456", "789", "1011");
        //originDto = 가져오기

        if(editDto.getId()!=null){
            originDto.setId(editDto.getId());
        }
        if(editDto.getPw()!=null){
            originDto.setPw(editDto.getPw());
        }
        if(editDto.getName()!=null){
            originDto.setName(editDto.getName());
        }
        if(editDto.getPhoneNum()!=null){
            originDto.setPhoneNum(editDto.getPhoneNum());
        }

        System.out.println(originDto.getId()+" "+originDto.getName()+" "+originDto.getPhoneNum());
        return ResponseEntity.ok().build();
    }

}