// foodiediary.user.dto.ValidationResultDto
package foodiediary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationResultDto {
    private boolean valid;
    private String message;
}
