package ru.mifi.ormplatform.web.dto;

/**
 * DTO-запрос, который отправляет студент, когда сдаёт задание.
 * <p>
 * Я сознательно оставляю здесь только идентификатор студента
 * и содержание решения (текст, ссылка на репозиторий и т.д.).
 */
public class SubmissionRequestDto {

    private Long studentId;
    private String content;

    public SubmissionRequestDto() {
        // Нужен пустой конструктор для Jackson
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
