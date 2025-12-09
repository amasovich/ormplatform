package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

/**
 * Профиль пользователя (PROFILE).
 */
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Внешний ключ PROFILE.user_id → USER.id. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 500)
    private String bio;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    public Profile() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

