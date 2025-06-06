package foodiediary.record.repository;

import foodiediary.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findTop10ByTitle(String title);

    List<Record> findTop10ByDescription(String description);

    List<Record> findTop10ByCoordinateXAndCoordinateY(BigDecimal coordinateX, BigDecimal coordinateY);

    List<Record> findTop10ByDate(LocalDate date);
}