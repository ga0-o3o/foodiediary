package foodiediary.record.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // 이미지 경로는 리스트로 저장 (JSON 문자열 형태로 저장해도 됨)
    @ElementCollection
    private List<String> imagePaths = new ArrayList<>();

    // 생성자, getter/setter 생략 가능
    public Record() {}

    public Record(String description, List<String> imagePaths) {
        this.description = description;
        this.imagePaths = imagePaths;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }
}
