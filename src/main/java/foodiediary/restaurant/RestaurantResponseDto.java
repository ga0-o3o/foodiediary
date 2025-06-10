package foodiediary.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantResponseDto {
	private Integer id;
	private String businessName;
	private Double coordX;
	private Double coordY;
	private String fullAddress;
}