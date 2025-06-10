package foodiediary.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
	
	private final RestaurantRepository restaurantRepository;
	
	public List<RestaurantResponseDto> findNearbyRestaurantsByLatLng(double latitude, double longitude) {
		// 위경도를 EPSG:5174(UTMK)로 변환
		double[] utmk = CoordinateConverter.toUtmk(latitude, longitude);
		double x = utmk[0];
		double y = utmk[1];
		double radius = 500.0; // 500m
		
		List<Object[]> results = restaurantRepository.findNearbyRestaurants(x, y, radius);
		
		return results.stream()
				.map(row -> new RestaurantResponseDto(
						(Integer) row[0],
						(String) row[1],
						row[2] != null ? ((Number) row[2]).doubleValue() : null,
						row[3] != null ? ((Number) row[3]).doubleValue() : null,
						(String) row[4]
				))
				.collect(Collectors.toList());
	}
}