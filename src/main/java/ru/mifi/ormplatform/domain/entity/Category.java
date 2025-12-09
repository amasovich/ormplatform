package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Категория курса (CATEGORY).
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название категории. */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Курсы, входящие в категорию (COURSE.category_id). */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    public Category() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}

