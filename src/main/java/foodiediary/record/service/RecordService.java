package foodiediary.record.service;

import foodiediary.record.dto.RecordResponseDto;
import foodiediary.record.dto.RecordUpdateRequestDto;
import foodiediary.record.dto.RecordWriteRequestDto;
import foodiediary.record.entity.Record;
import foodiediary.record.entity.RecordImage;
import foodiediary.record.repository.RecordImageRepository;
import foodiediary.record.repository.RecordRepository;
import foodiediary.s3.S3Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecordService {
    
    private final RecordRepository recordRepository;
    private final RecordImageRepository imageRepository;
    private final S3Service s3Service;
    
    @Transactional
    public Long writeRecord(RecordWriteRequestDto dto) throws IOException {
        // 1. Record 저장
        Record record = Record.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .coordinateX(dto.getCoordinateX())
                .coordinateY(dto.getCoordinateY())
                .date(dto.getDate())
                .author(dto.getAuthor())
                .visibility(dto.getVisibility())
                .like(0)
                .build();
        recordRepository.save(record);
        
        // 2. 이미지 업로드 및 경로 저장
        List<MultipartFile> images = dto.getImages();
        uploadImages(images, record.getId());
        return record.getId();
    }
    
    @Transactional
    public void updateRecord(RecordUpdateRequestDto dto) throws IOException {
        Record record = recordRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        
        // 1. 삭제할 이미지 처리
        if (dto.getDeleteImageUrls() != null && !dto.getDeleteImageUrls().isEmpty()) {
            for (String url : dto.getDeleteImageUrls()) {
                // 1) DB에서 삭제
                imageRepository.deleteByImagePath(url);
                // 2) S3에서 삭제
                s3Service.deleteImageByUrl(url);
            }
        }
        
        // 2. 새 이미지 업로드 전 개수 체크
        int currentImageCount = imageRepository.countByRecordId((record.getId()));
        int newImageCount = (dto.getNewImages() != null) ? dto.getNewImages().size() : 0;
        if (currentImageCount + newImageCount > 3)
            throw new IllegalArgumentException("이미지는 최대 3장까지 등록할 수 있습니다.");
        
        // 3. 새 이미지 업로드
        if (dto.getNewImages() != null) {
            List<MultipartFile> images = dto.getNewImages();
            uploadImages(images, record.getId());
        }
        
        // 4. 기타 필드 업데이트
        if (dto.getTitle() != null) record.setTitle(dto.getTitle());
        if (dto.getDescription() != null) record.setDescription(dto.getDescription());
        if (dto.getVisibility() != null) record.setVisibility(dto.getVisibility());
        
        // dirty checking
        recordRepository.save(record);
    }
    
    private void uploadImages(List<MultipartFile> images, Long id) throws IOException {
        if (images != null) {
            for (MultipartFile image : images) {
                String imageUrl = s3Service.uploadImage(image); // S3 업로드 후 URL 반환
                RecordImage imagePath = RecordImage.builder()
                        .recordId(id)
                        .imagePath(imageUrl)
                        .build();
                imageRepository.save(imagePath);
            }
        }
    }
    
    public List<RecordResponseDto> getRecordsByAuthor(String authorId) {
        List<Record> records = recordRepository.findByAuthor(authorId);
        return mapToResponseDto(records);
    }

    public List<RecordResponseDto> getFilteredRecords(String authorId, LocalDate date, BigDecimal coordinate_x,
                                                      BigDecimal coordinate_y, String description, String title) {
        if (authorId != null) {
            return getRecordsByAuthor(authorId);
        } else if (title != null) {
            return getRecordsByTitle(title);
        } else if(date != null){
            return getRecordsByDate(date);
        } else if(coordinate_x != null && coordinate_y != null){
            return getRecordsByCoordinate(coordinate_x, coordinate_y);
        } else if(description != null){
            return getRecordsByDescription(description);
        } else {
            return List.of(); // 조건 없을 경우
        }
    }

    private List<RecordResponseDto> getRecordsByDescription(String description) {
        List<Record> records = recordRepository.findByDescriptionIgnoreCaseContaining(description);
        return mapToResponseDto(records);
    }

    private List<RecordResponseDto> getRecordsByCoordinate(BigDecimal coordinateX, BigDecimal coordinateY) {
        List<Record> records = recordRepository.findByCoordinateXAndCoordinateY(coordinateX, coordinateY);
        return mapToResponseDto(records);
    }

    private List<RecordResponseDto> getRecordsByDate(LocalDate date) {
        List<Record> records = recordRepository.findByDate(date);
        return mapToResponseDto(records);
    }

    private List<RecordResponseDto> getRecordsByTitle(String title) {
        List<Record> records = recordRepository.findByTitle(title);
        return mapToResponseDto(records);
    }

    public List<RecordResponseDto> getPagedRecords(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5, Sort.by(Sort.Direction.DESC, "date"));
        Page<Record> page = recordRepository.findAllByOrderByDateDesc(pageable);
        return mapToResponseDto(page.getContent());
    }

    private List<RecordResponseDto> mapToResponseDto(List<Record> records) {
        return records.stream().map(record -> {
            List<String> imagePaths = imageRepository.findByRecordId(record.getId())
                    .stream()
                    .map(RecordImage::getImagePath)
                    .collect(Collectors.toList());

            return new RecordResponseDto(
                    record.getId(),
                    record.getTitle(),
                    record.getDescription(),
                    record.getCoordinateX(),
                    record.getCoordinateY(),
                    record.getDate(),
                    record.getAuthor(),
                    record.getVisibility(),
                    record.getLike(),
                    imagePaths
            );
        }).collect(Collectors.toList());
    }
}

