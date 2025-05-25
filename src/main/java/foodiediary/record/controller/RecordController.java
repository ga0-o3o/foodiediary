package foodiediary.record.controller;

import foodiediary.record.dto.RecordDto;
import foodiediary.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping(value = "/foodiediary/record/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImages(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("data") RecordDto data) {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("이미지가 없습니다");
        }

        try {
            recordService.saveRecordWithImages(files, data);
            return ResponseEntity.ok("✅ 저장 완료");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 실패");
        }
    }
}
