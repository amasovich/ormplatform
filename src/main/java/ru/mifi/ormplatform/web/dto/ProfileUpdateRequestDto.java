package ru.mifi.ormplatform.web.dto;

/**
 * DTO-запрос для обновления профиля пользователя.
 */
public class ProfileUpdateRequestDto {

    private String avatarUrl;
    private String bio;
    private String skills;

    private String socialLinkedIn;
    private String socialGithub;
    private String socialTelegram;

    public ProfileUpdateRequestDto() {
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSocialLinkedIn() {
        return socialLinkedIn;
    }

    public void setSocialLinkedIn(String socialLinkedIn) {
        this.socialLinkedIn = socialLinkedIn;
    }

    public String getSocialGithub() {
        return socialGithub;
    }

    public void setSocialGithub(String socialGithub) {
        this.socialGithub = socialGithub;
    }

    public String getSocialTelegram() {
        return socialTelegram;
    }

    public void setSocialTelegram(String socialTelegram) {
        this.socialTelegram = socialTelegram;
    }
}
