package foodiediary.record.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "record_image")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecordImage {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "record_id")
	private Long recordId;
	
	@Column(name = "image_path", length = 255)
	private String imagePath;
}