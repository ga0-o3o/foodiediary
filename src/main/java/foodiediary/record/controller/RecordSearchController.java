package foodiediary.record.controller;


import foodiediary.record.dto.RecordWriteRequestDto;
import foodiediary.record.service.RecordService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/foodiediary/record")
public class RecordSearchController {

    private RecordService recordService;

    @GetMapping
    public ResponseEntity<?> getRecord(RecordWriteRequestDto dto){
        return ResponseEntity.ok(recordService.getRecord(dto));
    }
}
