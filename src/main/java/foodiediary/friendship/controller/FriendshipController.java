package foodiediary.friendship.controller;

import foodiediary.friendship.dto.FriendshipRequestDto;
import foodiediary.friendship.dto.FriendshipResponseDto;
import foodiediary.friendship.entity.Friendship;
import foodiediary.friendship.service.FriendshipService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService svc;

    private String getMyId(HttpServletRequest req) {
        return (String) req.getAttribute("id");
    }

    @PostMapping("/request")
    public Friendship sendFriendRequest(HttpServletRequest req,
                                        @RequestBody FriendshipRequestDto dto) {
        String myId = getMyId(req);
        return svc.sendFriendRequest(myId, dto.getTargetId());
    }

    @DeleteMapping("/requests/{targetId}/cancel")
    public void cancelFriendRequest(HttpServletRequest req,
                                    @PathVariable String targetId) {
        String myId = getMyId(req);
        svc.cancelFriendRequest(myId, targetId);
    }

    @PostMapping("/requests/{requesterId}/accept")
    public void acceptFriendRequest(HttpServletRequest req,
                                    @PathVariable String requesterId) {
        String myId = getMyId(req);
        svc.acceptFriendRequest(myId, requesterId);
    }

    @PostMapping("/requests/{requesterId}/reject")
    public void rejectFriendRequest(HttpServletRequest req,
                                    @PathVariable String requesterId) {
        String myId = getMyId(req);
        svc.rejectFriendRequest(myId, requesterId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(HttpServletRequest req,
                             @PathVariable String friendId) {
        String myId = getMyId(req);
        svc.deleteFriend(myId, friendId);
    }

    @GetMapping
    public List<FriendshipResponseDto> getMyFriends(HttpServletRequest req) {
        String myId = getMyId(req);
        return svc.getMyFriends(myId).stream()
                .map(f -> new FriendshipResponseDto(
                        f.getId(),
                        // 내가 아니면 상대
                        f.getUserId().equals(myId) ? f.getFriendId() : f.getUserId(),
                        f.getStatus(),
                        f.getCreatedAt()
                ))
                .toList();
    }

    @GetMapping("/requests/received")
    public List<FriendshipResponseDto> getReceivedRequests(HttpServletRequest req) {
        String myId = getMyId(req);
        return svc.getReceivedRequests(myId).stream()
                .map(f -> new FriendshipResponseDto(
                        f.getId(),
                        // received 요청일 땐 userId가 요청자
                        f.getUserId(),
                        f.getStatus(),
                        f.getCreatedAt()
                ))
                .toList();
    }

    @GetMapping("/requests/sent")
    public List<FriendshipResponseDto> getSentRequests(HttpServletRequest req) {
        String myId = getMyId(req);
        return svc.getSentRequests(myId).stream()
                .map(f -> new FriendshipResponseDto(
                        f.getId(),
                        // sent 요청일 땐 friendId가 대상
                        f.getFriendId(),
                        f.getStatus(),
                        f.getCreatedAt()
                ))
                .toList();
    }
}
