package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Тег курса (TAG).
 */
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название тега. */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Курсы, помеченные этим тегом.
     * Физически связь реализована через таблицу COURSE_TAG.
     */
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    public Tag() {
    }

    // геттеры/сеттеры
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}

