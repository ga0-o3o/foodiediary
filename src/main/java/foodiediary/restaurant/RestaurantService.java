package foodiediary.restaurant;

import static foodiediary.restaurant.CoordinateConverter.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;

	public List<RestaurantResponseDto> findNearbyRestaurantsByLngLat(double longitude, double latitude) throws Exception {
		// 1. WGS84 → UTM-K 변환
		double[] utmk = convert4326To5174(longitude, latitude);

		// 2. 데이터베이스 조회
		List<Object[]> results = restaurantRepository.findNearbyRestaurants(
				utmk[0],
				utmk[1],
				1000.0
		);

		// 3. 결과 매핑 및 역변환
		return results.stream().map(row -> {
			// UTM-K → WGS84 역변환
			Double x = row[2] != null ? ((Number) row[2]).doubleValue() : null;
			Double y = row[3] != null ? ((Number) row[3]).doubleValue() : null;
					
					double[] wgs84 = null;
					try {
						wgs84 = (x != null && y != null) ?
								CoordinateConverter.convert5174To4326(x, y) :
								new double[]{0, 0};
					} catch(Exception e) {
						throw new RuntimeException(e);
					}
					
					return new RestaurantResponseDto(
					(Integer) row[0],
					(String) row[1],
					wgs84[0], // latitude
					wgs84[1], // longitude
					(String) row[4]
			);
		})
		.limit(20)
		.collect(Collectors.toList());
	}
}