package foodiediary.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {
	private final AmazonS3 amazonS3;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	public S3Service(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}
	
	public String uploadImage(MultipartFile image) throws IOException {
		String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(image.getContentType());
		metadata.setContentLength(image.getSize());
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata)); // 퍼블릭 읽기 권한
		return amazonS3.getUrl(bucket, fileName).toString();
	}
	
	public void deleteImageByUrl(String imageUrl) {
		String fileName = extractFileNameFromURL(imageUrl);
		amazonS3.deleteObject(bucket, fileName);
	}
	
	private String extractFileNameFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			String path = url.getPath();
			if (path.startsWith("/"+bucket+"/"))
				return path.substring(bucket.length() + 2); // '/' + bucket + '/'
			else if (path.startsWith("/"))
				return path.substring(1);
			return path;
		} catch(Exception e) {
			throw new IllegalArgumentException("잘못된 S3 이미지 URL입니다: " + imageUrl);
		}
	}
}