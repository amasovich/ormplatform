package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;
import ru.mifi.ormplatform.domain.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Пользователь учебной платформы.
 * Соответствует таблице USER.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя пользователя (name в ER). */
    @Column(nullable = false, length = 100)
    private String name;

    /** Уникальный e-mail. */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Роль пользователя (student / teacher / admin).
     * Хранится в колонке role как строка.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    /** Профиль пользователя (PROFILE.user_id FK). */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile;

    /** Курсы, которые ведёт пользователь как преподаватель (COURSE.teacher_id). */
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> taughtCourses = new ArrayList<>();

    /** Записи на курсы (ENROLLMENT.user_id). */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    /** Отправленные решения (SUBMISSION.student_id). */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();

    /** Результаты квизов (QUIZSUBMISSION.student_id). */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<QuizSubmission> quizSubmissions = new ArrayList<>();

    /** Отзывы о курсах (COURSEREVIEW.student_id). */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<CourseReview> courseReviews = new ArrayList<>();

    public User() {
    }

    public User(String name, String email, UserRole role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // геттеры/сеттеры
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public Profile getProfile() {
        return profile;
    }

    public List<Course> getTaughtCourses() {
        return taughtCourses;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public List<QuizSubmission> getQuizSubmissions() {
        return quizSubmissions;
    }

    public List<CourseReview> getCourseReviews() {
        return courseReviews;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setTaughtCourses(List<Course> taughtCourses) {
        this.taughtCourses = taughtCourses;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public void setQuizSubmissions(List<QuizSubmission> quizSubmissions) {
        this.quizSubmissions = quizSubmissions;
    }

    public void setCourseReviews(List<CourseReview> courseReviews) {
        this.courseReviews = courseReviews;
    }
}

