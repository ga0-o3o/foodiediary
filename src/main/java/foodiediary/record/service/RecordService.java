package foodiediary.record.service;

import foodiediary.record.dto.RecordWriteRequestDto;
import foodiediary.record.entity.Record;
import foodiediary.record.entity.RecordImagePath;
import foodiediary.record.repository.RecordImagePathRepository;
import foodiediary.record.repository.RecordRepository;
import foodiediary.s3.S3Service;
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
    private final S3Service s3Service; // 기존 S3Service (SDK 1.x)
    
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
                        .record(record)
                        .imagePath(imageUrl)
                        .build();
                imagePathRepository.save(imagePath);
            }
        }
        return record.getId();
    }
}

