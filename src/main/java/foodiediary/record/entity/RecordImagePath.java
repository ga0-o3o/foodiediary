package foodiediary.record.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecordImagePath {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "record_id")
	private Record record;
	
	@Column(name = "image_paths", length = 255)
	private String imagePath;
}