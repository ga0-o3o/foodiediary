package foodiediary.user.controller;

import foodiediary.user.dto.SignUpResultDto;
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
    public ResponseEntity<?> postSignUp(@RequestBody UserFormDto dto){
        SignUpResultDto result = userService.saveUser(dto);

        if (result.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}