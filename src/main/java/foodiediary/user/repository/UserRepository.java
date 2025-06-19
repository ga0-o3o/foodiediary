package foodiediary.user.repository;

import foodiediary.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<Object> findByIdAndPw(String id, String pw);

    List<User> findByIdContainingIgnoreCase(String partialId);

    List<User> findByNameContainingIgnoreCase(String partialName);
}