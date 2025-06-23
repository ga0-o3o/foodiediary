package foodiediary.record.repository;

import foodiediary.record.entity.Record;
import foodiediary.record.entity.RecordVisibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("SELECT r FROM Record r WHERE r.author = :author " +
            "AND (:title IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:date IS NULL OR r.date = :date) " +
            "AND (:coordinateX IS NULL OR r.coordinateX = :coordinateX) " +
            "AND (:coordinateY IS NULL OR r.coordinateY = :coordinateY) " +
            "AND (:description IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    List<Record> findFilteredRecordsForOwner(
            @org.springframework.data.repository.query.Param("author") String author,
            @org.springframework.data.repository.query.Param("title") String title,
            @org.springframework.data.repository.query.Param("date") LocalDate date,
            @org.springframework.data.repository.query.Param("coordinateX") BigDecimal coordinateX,
            @org.springframework.data.repository.query.Param("coordinateY") BigDecimal coordinateY,
            @org.springframework.data.repository.query.Param("description") String description
    );

    @Query("SELECT r FROM Record r WHERE r.author = :author " +
            "AND r.visibility = 'FRIEND' " +
            "AND (:title IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:date IS NULL OR r.date = :date) " +
            "AND (:coordinateX IS NULL OR r.coordinateX = :coordinateX) " +
            "AND (:coordinateY IS NULL OR r.coordinateY = :coordinateY) " +
            "AND (:description IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    List<Record> findFilteredRecordsForFriend(
            @org.springframework.data.repository.query.Param("author") String author,
            @org.springframework.data.repository.query.Param("title") String title,
            @org.springframework.data.repository.query.Param("date") LocalDate date,
            @org.springframework.data.repository.query.Param("coordinateX") BigDecimal coordinateX,
            @org.springframework.data.repository.query.Param("coordinateY") BigDecimal coordinateY,
            @org.springframework.data.repository.query.Param("description") String description
    );

    Page<Record> findByVisibilityOrderByLikeDesc(RecordVisibility visibility, Pageable pageable);

    Page<Record> findByAuthor(String author, Pageable pageable);

    Page<Record> findByAuthorAndVisibility(String authorId, RecordVisibility recordVisibility, Pageable pageable);

}