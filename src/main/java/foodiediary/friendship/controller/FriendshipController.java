package foodiediary.friendship.controller;

import foodiediary.friendship.dto.FriendshipRequestDto;
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
    public List<Friendship> getMyFriends(HttpServletRequest req) {
        String myId = getMyId(req);
        return svc.getMyFriends(myId);
    }

    @GetMapping("/requests/received")
    public List<Friendship> getReceivedRequests(HttpServletRequest req) {
        String myId = getMyId(req);
        return svc.getReceivedRequests(myId);
    }

    @GetMapping("/requests/sent")
    public List<Friendship> getSentRequests(HttpServletRequest req) {
        String myId = getMyId(req);
        return svc.getSentRequests(myId);
    }
}
