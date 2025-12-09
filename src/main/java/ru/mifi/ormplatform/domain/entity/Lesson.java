package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Урок внутри модуля (LESSON).
 */
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** LESSON.module_id → MODULE.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "video_url", length = 255)
    private String videoUrl;

    /** ASSIGNMENT.lesson_id. */
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

    public Lesson() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Module getModule() {
        return module;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }
}

