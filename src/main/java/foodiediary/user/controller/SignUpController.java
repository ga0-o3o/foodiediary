package foodiediary.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import foodiediary.user.dto.UserFormDto;

@RestController
public class SignUpController {

    @PostMapping("/foodiediary/user/signup")
    public ResponseEntity<Void> postSignUp(@RequestBody UserFormDto dto){
        //ResponseTestDto res = new ResponseTestDto(dto.getName(), dto.getId(), dto.getPw(), dto.getPhoneNum());
        //return res;
        return ResponseEntity.ok().build();
    }
}