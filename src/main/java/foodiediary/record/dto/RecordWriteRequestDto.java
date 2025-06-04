package foodiediary.record.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@Getter @AllArgsConstructor
public class RecordWriteRequestDto {
    private String title;
    private String description;
    private BigDecimal coordinateX;
    private BigDecimal coordinateY;
    private LocalDate date;
    private List<MultipartFile> images; // 최대 3개
}