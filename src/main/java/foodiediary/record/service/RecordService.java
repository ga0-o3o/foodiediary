package foodiediary.record.service;

import foodiediary.record.dto.RecordResponseDto;
import foodiediary.record.dto.RecordWriteRequestDto;
import foodiediary.record.entity.Record;
import foodiediary.record.entity.RecordImagePath;
import foodiediary.record.repository.RecordImagePathRepository;
import foodiediary.record.repository.RecordRepository;
import foodiediary.s3.S3Service;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecordService {
    
    private final RecordRepository recordRepository;
    private final RecordImagePathRepository imagePathRepository;
    private final S3Service s3Service;
    
    @Transactional
    public Long writeRecord(RecordWriteRequestDto dto, String authorId) throws IOException {
        // 1. Record 저장
        Record record = Record.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .coordinateX(dto.getCoordinateX())
                .coordinateY(dto.getCoordinateY())
                .date(dto.getDate())
                .author(authorId)
                .build();
        recordRepository.save(record);
        
        // 2. 이미지 업로드 및 경로 저장
        List<MultipartFile> images = dto.getImages();
        if (images != null) {
            for (MultipartFile image : images) {
                String imageUrl = s3Service.uploadImage(image); // S3 업로드 후 URL 반환
                RecordImagePath imagePath = RecordImagePath.builder()
                        .recordId(record.getId())
                        .imagePath(imageUrl)
                        .build();
                imagePathRepository.save(imagePath);
            }
        }
        return record.getId();
    }
    
    public List<RecordResponseDto> getRecordsByAuthor(String authorId) {
        List<Record> records = recordRepository.findByAuthor(authorId);
        return records.stream().map(record -> {
            List<String> imagePaths = imagePathRepository.findByRecordId(record.getId())
                    .stream()
                    .map(RecordImagePath::getImagePath)
                    .collect(Collectors.toList());
            return new RecordResponseDto(
                    record.getId(),
                    record.getTitle(),
                    record.getDescription(),
                    record.getCoordinateX(),
                    record.getCoordinateY(),
                    record.getDate(),
                    imagePaths
            );
        }).collect(Collectors.toList());
    }

//    public List<Record> findByOneNonNullField(Record probe) {
//        if (probe.getTitle() != null) {
//            return recordRepository.findTop10ByTitle(probe.getTitle());
//        } else if (probe.getDescription() != null) {
//            return recordRepository.findTop10ByDescription(probe.getDescription());
//        } else if (probe.getCoordinateX() != null && probe.getCoordinateY() != null) {
//            return recordRepository.findTop10ByCoordinateXAndCoordinateY(probe.getCoordinateX(), probe.getCoordinateY());
//        } else if (probe.getDate() != null) {
//            return recordRepository.findTop10ByDate(probe.getDate());
//        } else {
//            return List.of(); // 전부 null인 경우
//        }
//    }
//
//    public List<Record> getRecord(RecordWriteRequestDto dto) {
//        Record probe = new Record();
//        probe.setTitle(dto.getTitle());
//        probe.setDescription(dto.getDescription());
//        probe.setCoordinateX(dto.getCoordinateX());
//        probe.setCoordinateY(dto.getCoordinateY());
//        probe.setDate(dto.getDate());
//
//        return findByOneNonNullField(probe);
//    }
}

