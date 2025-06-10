package foodiediary.restaurant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	
	@Query(value = "SELECT r.id, r.business_name, r.coord_x, r.coord_y, r.full_address " +
			"FROM restaurant r " +
			"WHERE SQRT(POWER(r.coord_x - :latitude, 2) + POWER(r.coord_y - :longitude, 2)) <= :radius", nativeQuery = true)
	List<Object[]> findNearbyRestaurants(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius);
}
