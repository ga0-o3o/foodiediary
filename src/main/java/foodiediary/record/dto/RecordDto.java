package foodiediary.record.dto;

public class RecordDto {
    private String text;

    public RecordDto() {} // ← 기본 생성자 추가!

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


