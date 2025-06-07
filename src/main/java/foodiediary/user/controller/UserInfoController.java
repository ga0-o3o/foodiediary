package foodiediary.user.controller;

import foodiediary.user.dto.ResponseTestDto;
import foodiediary.user.dto.UserFormDto;
import foodiediary.user.entity.User;
import foodiediary.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class UserInfoController {

    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/foodiediary/user/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request){
        User user = userService.getUserInfo((String) request.getAttribute("id"));
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/foodiediary/user/info")
    public ResponseEntity<Void> editUserInfo(HttpServletRequest request, @RequestBody UserFormDto editDto){
        String userId = (String) request.getAttribute("id");
        editDto.setId(userId);

        if(userService.updateUserInfo(editDto)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

}