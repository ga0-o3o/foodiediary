package foodiediary.s3;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {
	private final S3Service s3Service;
	
	@PostMapping("foodiediary/upload")
	public ResponseEntity<String> upload(@RequestParam("image") MultipartFile file) throws IOException {
		String imageUrl = s3Service.uploadImage(file);
		return ResponseEntity.ok(imageUrl);
	}
}