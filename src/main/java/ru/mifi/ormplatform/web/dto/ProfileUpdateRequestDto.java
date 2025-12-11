package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.Size;

/**
 * DTO-запрос для обновления профиля пользователя.
 *
 * Поддерживает частичное обновление:
 *  - bio
 *  - avatarUrl
 */
public class ProfileUpdateRequestDto {

    /**
     * URL аватара пользователя.
     * Может быть null — в этом случае не обновляется.
     */
    @Size(max = 255, message = "avatarUrl не должен превышать 255 символов")
    private String avatarUrl;

    /**
     * Короткая биография пользователя.
     * Может быть null — в этом случае не обновляется.
     */
    @Size(max = 500, message = "bio не должен превышать 500 символов")
    private String bio;

    public ProfileUpdateRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    // ======================
    //        SETTERS
    // ======================

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
