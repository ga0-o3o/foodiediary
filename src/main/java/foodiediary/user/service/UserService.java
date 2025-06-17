package foodiediary.user.service;

import foodiediary.user.dto.LoginDto;
import foodiediary.user.dto.UserFormDto;
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

    public boolean isValidUserForm(UserFormDto userFormDto){
        if (userFormDto.getId().length() > 15) return false;
        if (userFormDto.getPw().length() < 10 || userFormDto.getPw().length() > 15) return false;


        // 영문자와 숫자 반드시 포함, '_' 또는 '.'만 특수문자로 허용
        String idRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d_.]{1,15}$";
        boolean idValidate = Pattern.matches(idRegex, userFormDto.getId());
        System.out.println("id 양식 "+ idValidate);

        String pwRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{10,15}$";
        boolean pwValidate = Pattern.matches(pwRegex, userFormDto.getPw());
        System.out.println("pw 양식 "+ pwValidate);

        String nameRegex = "^[가-힣a-zA-Z]+$";
        boolean nameValidate = Pattern.matches(nameRegex, userFormDto.getName());
        System.out.println("name 양식 "+ nameValidate);

        String phoneRegex = "^\\d{11}$";
        boolean phoneValidate = Pattern.matches(phoneRegex, userFormDto.getPhoneNum());
        System.out.println("phone 양식 "+ phoneValidate);

        boolean result = idValidate && pwValidate && nameValidate && phoneValidate;;
        System.out.println("회원가입 양식 "+ result);
        return result;
    }

    public boolean saveUser(UserFormDto userFormDto){
        if(isValidUserForm(userFormDto) && !userRepository.existsById(userFormDto.getId())){
            userRepository.save(userFormDto.getUserEntity());
            return true;
        } else {
            return false;
        }
    }

    public User getUserInfo(String id){
        User user = userRepository.findById(id).get();
        return user;
    }

    public boolean updateUserInfo(UserFormDto userFormDto){
        Optional<User> optionalUser = userRepository.findById(userFormDto.getId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(userFormDto.getPw() != null){
                user.setPw(userFormDto.getPw());
            }

            if(userFormDto.getName() != null){
                user.setName(userFormDto.getName());
            }

            if(userFormDto.getPhoneNum() != null){
                user.setPhoneNum(userFormDto.getPhoneNum());
            }

            if(isValidUserForm(userFormDto.change2UserFormDto(user))){
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
