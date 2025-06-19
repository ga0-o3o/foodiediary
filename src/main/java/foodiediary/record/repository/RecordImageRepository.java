package foodiediary.record.repository;

import foodiediary.record.entity.RecordImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecordImageRepository extends JpaRepository<RecordImage, Long> {
	List<RecordImage> findByRecordId(Long id);
	void deleteByRecordId(Long id);
}