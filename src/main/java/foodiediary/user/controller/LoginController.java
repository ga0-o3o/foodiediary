package foodiediary.user.controller;

import foodiediary.security.JwtProvider;
import foodiediary.user.dto.LoginDto;
import foodiediary.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    private final JwtProvider jwtProvider;

    private final UserService userService;
    public LoginController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/foodiediary/user/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        System.out.println("ID: " + loginDto.getId());

        if (userService.isSuccessLogin(loginDto)) {
            String token = jwtProvider.generateToken(loginDto.getId());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다");
        }
    }
}
