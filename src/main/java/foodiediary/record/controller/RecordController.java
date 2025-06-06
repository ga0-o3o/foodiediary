package foodiediary.record.controller;

import foodiediary.record.dto.RecordResponseDto;
import foodiediary.record.dto.RecordWriteRequestDto;
import foodiediary.record.service.RecordService;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/foodiediary/record")
public class RecordController {
    
    private final RecordService recordService;
    
    @PostMapping("/write")
    public ResponseEntity<Long> writeRecord(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam("coordinate_x") BigDecimal coordinateX,
            @RequestParam("coordinate_y") BigDecimal coordinateY,
            @RequestParam("date") LocalDate date,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam("authorId") String authorId // 유저 정보를 직접 파라미터로 받음
    ) throws IOException {
        RecordWriteRequestDto dto = new RecordWriteRequestDto(title, description, coordinateX, coordinateY, date, images);
        Long recordId = recordService.writeRecord(dto, authorId);
        return ResponseEntity.ok(recordId);
    }
    
    @GetMapping("/list")
    public List<RecordResponseDto> getRecordsByAuthor(@RequestParam String authorId) {
        return recordService.getRecordsByAuthor(authorId);
    }
}