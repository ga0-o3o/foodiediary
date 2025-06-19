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
    private List<MultipartFile> images; // 최대 3개
}