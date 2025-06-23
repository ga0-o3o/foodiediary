package foodiediary.record.dto;

import foodiediary.record.entity.RecordVisibility;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter @AllArgsConstructor
public class RecordUpdateRequestDto {
    private Long id;
    private String title;
    private String description;
    private RecordVisibility visibility;
    // 삭제할 이미지 URL 목록
    private List<String> deleteImageUrls;
    // 새로 추가할 이미지 파일 목록
    private List<MultipartFile> newImages;
}