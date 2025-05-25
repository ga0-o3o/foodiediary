package foodiediary.friendship.controller;

import foodiediary.friendship.dto.FriendshipRequestDto;
import foodiediary.friendship.service.FriendshipService;
import foodiediary.friendship.entity.Friendship;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/foodiediary/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // 친구 요청 보내기
    @PostMapping("/request")
    public Friendship sendFriendRequest(HttpServletRequest request, @RequestBody FriendshipRequestDto dto) {
        Long myId = Long.valueOf((String) request.getAttribute("id")); // (실제로는 로그인 사용자 ID)
        return friendshipService.sendFriendRequest(myId, dto.getTargetId());
    }

    // 친구 요청 취소
    @DeleteMapping("/requests/{targetId}/cancel")
    public void cancelFriendRequest(HttpServletRequest request, @PathVariable Long targetId) {
        Long myId = Long.valueOf((String) request.getAttribute("id"));
        friendshipService.cancelFriendRequest(myId, targetId);
    }

    // 친구 요청 수락
    @PostMapping("/requests/{requesterId}/accept")
    public void acceptFriendRequest(HttpServletRequest request, @PathVariable Long requesterId) {
        Long myId = Long.valueOf((String) request.getAttribute("id"));
        friendshipService.acceptFriendRequest(myId, requesterId);
    }

    // 친구 요청 거절
    @PostMapping("/requests/{requesterId}/reject")
    public void rejectFriendRequest(HttpServletRequest request, @PathVariable Long requesterId) {
        Long myId = Long.valueOf((String) request.getAttribute("id"));
        friendshipService.rejectFriendRequest(myId, requesterId);
    }

    // 친구 삭제
    @DeleteMapping("/{friendId}")
    public void deleteFriend(HttpServletRequest request, @PathVariable Long friendId) {
        Long myId = Long.valueOf((String) request.getAttribute("id"));
        friendshipService.deleteFriend(myId, friendId);
    }

    // 내 친구 목록 조회
    @GetMapping
    public List<Friendship> getMyFriends(HttpServletRequest request) {
        Long myId = Long.valueOf((String) request.getAttribute("id"));
        return friendshipService.getMyFriends(myId);
    }

    // 받은 친구 요청 목록 조회
    @GetMapping("/requests/received")
    public List<Friendship> getReceivedRequests(HttpServletRequest request) {
        Long myId = Long.valueOf((String) request.getAttribute("id"));
        return friendshipService.getReceivedRequests(myId);
    }

    // 보낸 친구 요청 목록 조회
    @GetMapping("/requests/sent")
    public List<Friendship> getSentRequests(HttpServletRequest request) {
        Long myId = Long.valueOf((String) request.getAttribute("id"));
        return friendshipService.getSentRequests(myId);
    }

    // 친구 검색은 User 필요
}
