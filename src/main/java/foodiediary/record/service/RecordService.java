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
        Record record = recordRepository.findById(dto.getId()).get();
        record.setTitle(dto.getTitle());
        record.setDescription(dto.getDescription());
        record.setVisibility(dto.getVisibility());
        recordRepository.save(record);
        
        List<RecordImage> oldImages = imageRepository.findByRecordId(record.getId());
        for (RecordImage recordImage : oldImages) {
            s3Service.deleteImageByUrl(recordImage.getImagePath());
        }
        
        imageRepository.deleteByRecordId(record.getId());
        
        List<MultipartFile> images = dto.getImages();
        uploadImages(images, record.getId());
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

