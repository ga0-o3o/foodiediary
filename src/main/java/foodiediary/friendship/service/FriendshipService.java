package foodiediary.friendship.service;

import foodiediary.friendship.repository.FriendshipRepository;
import foodiediary.friendship.entity.Friendship;
import foodiediary.friendship.entity.FriendshipStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendshipService {

    private final FriendshipRepository repo;

    public FriendshipService(FriendshipRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Friendship sendFriendRequest(String myId, String targetId) {
        if (myId.equals(targetId)) {
            throw new IllegalArgumentException("자기 자신에게 친구 요청 불가");
        }

        String[] ids = sorted(myId, targetId);
        String userId = ids[0], friendId = ids[1];

        repo.findFriendshipBothDirections(userId, friendId)
                .ifPresent(f -> {
                    switch (f.getStatus()) {
                        case PENDING:
                            throw new IllegalStateException("이미 요청 중");
                        case ACCEPTED:
                            throw new IllegalStateException("이미 친구임");
                        case REJECTED:
                            throw new IllegalStateException("이전 요청이 거절된 상태");
                    }
                });

        Friendship f = new Friendship();
        f.setUserId(userId);
        f.setFriendId(friendId);
        f.setStatus(FriendshipStatus.PENDING);
        return repo.save(f);
    }

    @Transactional
    public void cancelFriendRequest(String myId, String targetId) {
        String[] ids = sorted(myId, targetId);
        Friendship f = repo.findFriendshipBothDirections(ids[0], ids[1])
                .orElseThrow(() -> new IllegalStateException("친구 요청이 없습니다"));
        if (f.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아님");
        }
        repo.delete(f);
    }

    @Transactional
    public void acceptFriendRequest(String myId, String requesterId) {
        String[] ids = sorted(myId, requesterId);
        Friendship f = repo.findFriendshipBothDirections(ids[0], ids[1])
                .orElseThrow(() -> new IllegalStateException("친구 요청이 없습니다"));
        if (f.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아님");
        }
        f.setStatus(FriendshipStatus.ACCEPTED);
        repo.save(f);
    }

    @Transactional
    public void rejectFriendRequest(String myId, String requesterId) {
        String[] ids = sorted(myId, requesterId);
        Friendship f = repo.findFriendshipBothDirections(ids[0], ids[1])
                .orElseThrow(() -> new IllegalStateException("친구 요청이 없습니다"));
        if (f.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아님");
        }
        f.setStatus(FriendshipStatus.REJECTED);
        repo.save(f);
    }

    @Transactional
    public void deleteFriend(String myId, String friendId) {
        String[] ids = sorted(myId, friendId);
        Friendship f = repo.findFriendshipBothDirections(ids[0], ids[1])
                .orElseThrow(() -> new IllegalStateException("친구 관계가 없습니다"));
        if (f.getStatus() != FriendshipStatus.ACCEPTED) {
            throw new IllegalStateException("친구 상태가 아닙니다");
        }
        repo.delete(f);
    }

    public List<Friendship> getMyFriends(String myId) {
        return repo.findMyFriends(myId);
    }

    public List<Friendship> getReceivedRequests(String myId) {
        return repo.findReceivedRequests(myId);
    }

    public List<Friendship> getSentRequests(String myId) {
        return repo.findSentRequests(myId);
    }

    private String[] sorted(String a, String b) {
        return a.compareTo(b) <= 0
                ? new String[]{a, b}
                : new String[]{b, a};
    }
}