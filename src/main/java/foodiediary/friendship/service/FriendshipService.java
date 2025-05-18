package foodiediary.friendship.service;

import foodiediary.friendship.dao.FriendshipRepository;
import foodiediary.friendship.entity.Friendship;
import foodiediary.friendship.entity.FriendshipStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Transactional
    public Friendship sendFriendRequest(Long myId, Long targetId) {
        if (myId.equals(targetId)) throw new IllegalArgumentException("자기 자신에게 친구 요청 불가");
        Long userId = Math.min(myId, targetId);
        Long friendId = Math.max(myId, targetId);

        friendshipRepository.findFriendshipBothDirections(userId, friendId)
                .ifPresent(f -> {
                    if (f.getStatus() == FriendshipStatus.PENDING)
                        throw new IllegalStateException("이미 요청 중");
                    if (f.getStatus() == FriendshipStatus.ACCEPTED)
                        throw new IllegalStateException("이미 친구임");
                });

        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(FriendshipStatus.PENDING);

        return friendshipRepository.save(friendship);
    }

    @Transactional
    public void cancelFriendRequest(Long myId, Long targetId) {
        Long userId = Math.min(myId, targetId);
        Long friendId = Math.max(myId, targetId);

        Friendship friendship = friendshipRepository.findFriendshipBothDirections(userId, friendId)
                .orElseThrow(() -> new IllegalStateException("친구 요청이 없습니다"));
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아님");
        }
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public void acceptFriendRequest(Long myId, Long requesterId) {
        Long userId = Math.min(myId, requesterId);
        Long friendId = Math.max(myId, requesterId);

        Friendship friendship = friendshipRepository.findFriendshipBothDirections(userId, friendId)
                .orElseThrow(() -> new IllegalStateException("친구 요청이 없습니다"));
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아님");
        }
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long myId, Long requesterId) {
        Long userId = Math.min(myId, requesterId);
        Long friendId = Math.max(myId, requesterId);

        Friendship friendship = friendshipRepository.findFriendshipBothDirections(userId, friendId)
                .orElseThrow(() -> new IllegalStateException("친구 요청이 없습니다"));
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아님");
        }
        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void deleteFriend(Long myId, Long friendId) {
        Long userId = Math.min(myId, friendId);
        Long fId = Math.max(myId, friendId);

        Friendship friendship = friendshipRepository.findFriendshipBothDirections(userId, fId)
                .orElseThrow(() -> new IllegalStateException("친구 관계가 없습니다"));
        if (friendship.getStatus() != FriendshipStatus.ACCEPTED) {
            throw new IllegalStateException("이미 친구 아님");
        }
        friendshipRepository.delete(friendship);
    }

    public List<Friendship> getMyFriends(Long myId) {
        return friendshipRepository.findMyFriends(myId);
    }

    public List<Friendship> getReceivedRequests(Long myId) {
        return friendshipRepository.findReceivedRequests(myId);
    }

    public List<Friendship> getSentRequests(Long myId) {
        return friendshipRepository.findSentRequests(myId);
    }

    // 친구 검색 추가 예정 (user 필요)
    // public List<User> searchFriends(String keyword) {...}
}
