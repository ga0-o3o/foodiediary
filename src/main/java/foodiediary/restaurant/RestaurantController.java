package foodiediary.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/foodiediary/restaurant")
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	
	@GetMapping("/nearby")
	public List<RestaurantResponseDto> getNearbyRestaurants(
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude
	) {
		return restaurantService.findNearbyRestaurantsByLatLng(latitude, longitude);
	}
}