package foodiediary.user.controller;

import foodiediary.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import foodiediary.user.dto.UserFormDto;

@CrossOrigin(origins = "*")
@RestController
public class SignUpController {


    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/foodiediary/user/signup")
    public ResponseEntity<Void> postSignUp(@RequestBody UserFormDto dto){
        System.out.println(dto.getId());
        boolean ok = userService.saveUser(dto);
        if(ok){
            return ResponseEntity.ok().build();
        } else {
            // 회원가입 불가 사유를 전달하는게 좋을 듯?
            return ResponseEntity.badRequest().build();
        }
    }
}