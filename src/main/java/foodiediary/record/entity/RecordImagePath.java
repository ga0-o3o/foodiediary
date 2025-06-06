package foodiediary.record.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecordImagePath {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "record_id")
	private Long recordId;
	
	@Column(name = "image_paths", length = 255)
	private String imagePath;
}