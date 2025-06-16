package foodiediary.friendship.dto;

import foodiediary.friendship.entity.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FriendshipResponseDto {
    private Long id;
    private String otherId;           // 내 ID가 아닌 상대방 ID
    private FriendshipStatus status;
    private LocalDateTime createdAt;
}
