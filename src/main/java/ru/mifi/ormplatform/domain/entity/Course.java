package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** title в ER. */
    @Column(nullable = false, length = 200)
    private String title;

    /** description в ER. */
    @Column(nullable = false, length = 2000)
    private String description;

    /** CATEGORY.id → COURSE.category_id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** USER.id (teacher) → COURSE.teacher_id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    /** duration (строка вида "6 недель"). */
    @Column(length = 50)
    private String duration;

    /** startDate из ER (LocalDate). */
    private LocalDate startDate;

    /** Модули курса (MODULE.course_id). */
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Module> modules = new ArrayList<>();

    /** Записи студентов (ENROLLMENT.course_id). */
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    /** Отзывы (COURSEREVIEW.course_id). */
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseReview> reviews = new ArrayList<>();

    /**
     * Теги курса.
     * Физически реализованы таблицей COURSE_TAG (course_id, tag_id).
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_tag",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    /** Квизы по курсу (согласно ER курс может иметь несколько квизов). */
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();

    public Course() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public User getTeacher() {
        return teacher;
    }

    public String getDuration() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<CourseReview> getReviews() {
        return reviews;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public void setReviews(List<CourseReview> reviews) {
        this.reviews = reviews;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}

