package foodiediary.user.service;

import foodiediary.user.dto.LoginDto;
import foodiediary.user.dto.SignUpResultDto;
import foodiediary.user.dto.UserFormDto;
import foodiediary.user.dto.ValidationResultDto;
import foodiediary.user.entity.User;
import foodiediary.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean isSuccessLogin(LoginDto loginDto){
        return userRepository.findByIdAndPw(loginDto.getId(), loginDto.getPw()).isPresent();
    }

    public ValidationResultDto validateUserForm(UserFormDto userFormDto) {
        // ID
        if (userFormDto.getId() == null || userFormDto.getId().isBlank()) {
            return new ValidationResultDto(false, "ID는 필수 입력 항목입니다.");
        }
        if (userFormDto.getId().length() > 15) {
            return new ValidationResultDto(false, "ID는 15자 이하여야 합니다.");
        }
        String idRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d_.]{1,15}$";
        if (!Pattern.matches(idRegex, userFormDto.getId())) {
            return new ValidationResultDto(false, "ID는 영문자와 숫자를 포함해야 하며, '_', '.'만 특수문자로 허용됩니다.");
        }

        // 비밀번호
        if (userFormDto.getPw() == null || userFormDto.getPw().isBlank()) {
            return new ValidationResultDto(false, "비밀번호는 필수 입력 항목입니다.");
        }
        if (userFormDto.getPw().length() < 10 || userFormDto.getPw().length() > 15) {
            return new ValidationResultDto(false, "비밀번호는 10자 이상 15자 이하이어야 합니다.");
        }
        String pwRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{10,15}$";
        if (!Pattern.matches(pwRegex, userFormDto.getPw())) {
            return new ValidationResultDto(false, "비밀번호는 영문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.");
        }

        // 이름
        if (userFormDto.getName() == null || userFormDto.getName().isBlank()) {
            return new ValidationResultDto(false, "이름은 필수 입력 항목입니다.");
        }
        String nameRegex = "^[가-힣a-zA-Z]+$";
        if (!Pattern.matches(nameRegex, userFormDto.getName())) {
            return new ValidationResultDto(false, "이름은 한글 또는 영문자만 입력할 수 있습니다.");
        }

        // 전화번호
        if (userFormDto.getPhoneNum() == null || userFormDto.getPhoneNum().isBlank()) {
            return new ValidationResultDto(false, "전화번호는 필수 입력 항목입니다.");
        }
        String phoneRegex = "^\\d{11}$";
        if (!Pattern.matches(phoneRegex, userFormDto.getPhoneNum())) {
            return new ValidationResultDto(false, "전화번호는 11자리 숫자만 입력해야 합니다.");
        }

        return new ValidationResultDto(true, "모든 양식이 유효합니다.");
    }


    public SignUpResultDto saveUser(UserFormDto userFormDto) {
        ValidationResultDto validationResult = validateUserForm(userFormDto);

        if (!validationResult.isValid()) {
            return new SignUpResultDto(false, validationResult.getMessage());
        }

        if (userRepository.existsById(userFormDto.getId())) {
            return new SignUpResultDto(false, "이미 사용 중인 ID입니다.");
        }

        userRepository.save(userFormDto.getUserEntity());
        return new SignUpResultDto(true, "회원가입이 성공적으로 완료되었습니다.");
    }


    public User getUserInfo(String id){
        User user = userRepository.findById(id).get();
        return user;
    }

    public boolean updateUserInfo(UserFormDto userFormDto) {
        Optional<User> optionalUser = userRepository.findById(userFormDto.getId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 입력된 값만 업데이트
            if (userFormDto.getPw() != null) {
                user.setPw(userFormDto.getPw());
            }
            if (userFormDto.getName() != null) {
                user.setName(userFormDto.getName());
            }
            if (userFormDto.getPhoneNum() != null) {
                user.setPhoneNum(userFormDto.getPhoneNum());
            }

            // 변경된 user 엔티티를 다시 DTO로 바꿔서 유효성 검사
            UserFormDto updatedDto = userFormDto.change2UserFormDto(user);
            ValidationResultDto validationResult = validateUserForm(updatedDto);

            if (validationResult.isValid()) {
                userRepository.save(user);
                return true;
            } else {
                System.out.println("정보 수정 실패 사유: " + validationResult.getMessage());
                return false;
            }
        }

        return false;
    }

}
