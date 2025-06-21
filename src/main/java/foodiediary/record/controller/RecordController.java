package foodiediary.record.controller;

import foodiediary.record.dto.RecordResponseDto;
import foodiediary.record.dto.RecordUpdateRequestDto;
import foodiediary.record.dto.RecordWriteRequestDto;
import foodiediary.record.entity.RecordVisibility;
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
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("coordinate_x") BigDecimal coordinateX,
            @RequestParam("coordinate_y") BigDecimal coordinateY,
            @RequestParam("date") LocalDate date,
            @RequestParam("authorId") String authorId, // 유저 정보를 직접 파라미터로 받음
            @RequestParam("visibility") String visibilityStr,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        RecordVisibility visibility = RecordVisibility.valueOf(visibilityStr);
        RecordWriteRequestDto dto = new RecordWriteRequestDto(title, description, coordinateX, coordinateY, date, authorId,
                                                                visibility, images);
        Long recordId = recordService.writeRecord(dto);
        return ResponseEntity.ok(recordId);
    }
    
    @PatchMapping("/update")
    public ResponseEntity<?> updateRecord(
            @RequestParam("id") Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "visibility", required = false) String visibilityStr,
            @RequestParam(value = "deleteImageUrls", required = false) List<String> deleteImageUrls,
            @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages
    ) throws IOException {
        RecordVisibility visibility = visibilityStr != null ? RecordVisibility.valueOf(visibilityStr) : null;
        RecordUpdateRequestDto dto = new RecordUpdateRequestDto(id, title, description, visibility, deleteImageUrls, newImages);
        recordService.updateRecord(dto);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/list")
    public List<RecordResponseDto> getRecords(
            @RequestParam(required = false) String authorId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String coordinateX,
            @RequestParam(required = false) String coordinateY,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String title) {

        BigDecimal x = null;
        BigDecimal y = null;
        if (coordinateX != null && coordinateY != null) {
            x = new BigDecimal(coordinateX);
            y = new BigDecimal(coordinateY);
        }

        LocalDate parsedDate = null;
        if(date != null){
            parsedDate = LocalDate.parse(date);
        }

        return recordService.getFilteredRecords(authorId, parsedDate, x, y, description, title);
    }

    @GetMapping("/page")
    public List<RecordResponseDto> getInitialPage(@RequestParam(required = false) String pageNum){
        int page;
        if(pageNum == null){
            page = 1;
        } else {
            page = Integer.parseInt(pageNum);
        }

        return recordService.getPagedRecords(page);
    }

}