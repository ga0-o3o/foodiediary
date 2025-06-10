package foodiediary.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity @Getter @Table(name = "restaurant")
public class Restaurant {
	@Id private Integer id;
	@Column(name = "business_name")
	private String businessName;
	@Column(name = "coord_x")
	private Double coordX;
	@Column(name = "coord_y")
	private Double coordY;
	@Column(name = "full_address")
	private String fullAddress;
	// ... (필요시 추가 필드)
}