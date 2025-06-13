package foodiediary.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantResponseDto {
	private Integer id;
	private String businessName;
	private Double latitue;
	private Double longitude;
	private String fullAddress;
}