package foodiediary.record.repository;

import foodiediary.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByAuthor(String author); // 사용자 이름으로 기록 찾기

    List<Record> findByTitle(String title);

    List<Record> findByDescription(String description);

    List<Record> findByCoordinateXAndCoordinateY(BigDecimal coordinateX, BigDecimal coordinateY);

    List<Record> findByDate(LocalDate date);

    List<Record> findByDescriptionIgnoreCaseContaining(String keyword);

}