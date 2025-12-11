package ru.mifi.ormplatform.web.dto;

/**
 * DTO профиля пользователя.
 *
 * Используется для возврата информации о профиле в REST API.
 * Содержит:
 *  - идентификатор профиля,
 *  - идентификатор пользователя,
 *  - имя пользователя (денормализованное поле для удобства фронтенда),
 *  - биографию,
 *  - ссылку на аватар.
 */
public class ProfileDto {

    private Long id;
    private Long userId;

    /**
     * Имя пользователя — денормализованное поле,
     * чтобы фронт не делал отдельный запрос на /api/users/{id}.
     */
    private String userName;

    /** Короткая биография пользователя. */
    private String bio;

    /** URL изображения-аватара. */
    private String avatarUrl;

    public ProfileDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    // ======================
    //        SETTERS
    // ======================

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
