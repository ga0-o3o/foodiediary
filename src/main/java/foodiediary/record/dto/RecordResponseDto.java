package foodiediary.record.dto;

import foodiediary.record.entity.RecordVisibility;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordResponseDto {
	private Long id;
	private String title;
	private String description;
	private BigDecimal coordinateX;
	private BigDecimal coordinateY;
	private LocalDate date;
	private String author;
	private RecordVisibility visibility;
	private int like;
	private List<String> imagePaths; // 최대 3개
}
