package foodiediary.record.service;

import foodiediary.record.dto.RecordDto;
import foodiediary.record.entity.Record;
import foodiediary.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    private final String uploadDir = "uploadedImage";

    public void saveRecordWithImages(List<MultipartFile> files, RecordDto data) throws IOException {
        List<String> savedPaths = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get(uploadDir, filename);
            Files.createDirectories(savePath.getParent());
            Files.write(savePath, file.getBytes());

            savedPaths.add(savePath.toString());
        }

        Record entry = new Record(data.getText(), savedPaths);
        recordRepository.save(entry);
    }
}

