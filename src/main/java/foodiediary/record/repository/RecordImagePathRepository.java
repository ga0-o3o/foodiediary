package foodiediary.record.repository;

import foodiediary.record.entity.RecordImagePath;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordImagePathRepository extends JpaRepository<RecordImagePath, Long> {
	List<RecordImagePath> findByRecordId(Long id);
}