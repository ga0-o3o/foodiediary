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
			@RequestParam("longitude") double longitude,
			@RequestParam("latitude") double latitude) throws Exception {
		return restaurantService.findNearbyRestaurantsByLngLat(longitude, latitude);
	}
}