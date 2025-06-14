package foodiediary.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantResponseDto {
	private Integer id;
	private String businessName;
	private Double longitude;
	private Double latitude;
	private String fullAddress;
}