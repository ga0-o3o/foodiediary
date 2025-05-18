package foodiediary.friendship.dao;

import foodiediary.friendship.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // userId < friendId로 만 저장하는 전제
    @Query("SELECT f FROM Friendship f WHERE ((f.userId = :userId AND f.friendId = :friendId) OR (f.userId = :friendId AND f.friendId = :userId))")
    Optional<Friendship> findFriendshipBothDirections(Long userId, Long friendId);

    @Query("SELECT f FROM Friendship f WHERE (f.userId = :userId OR f.friendId = :userId) AND f.status = 'ACCEPTED'")
    List<Friendship> findMyFriends(Long userId);

    @Query("SELECT f FROM Friendship f WHERE f.friendId = :userId AND f.status = 'PENDING'")
    List<Friendship> findReceivedRequests(Long userId);

    @Query("SELECT f FROM Friendship f WHERE f.userId = :userId AND f.status = 'PENDING'")
    List<Friendship> findSentRequests(Long userId);

    @Query("SELECT f FROM Friendship f WHERE ((f.userId = :userId AND f.friendId = :friendId) OR (f.userId = :friendId AND f.friendId = :userId))")
    Optional<Friendship> findExistingRelation(Long userId, Long friendId);
}