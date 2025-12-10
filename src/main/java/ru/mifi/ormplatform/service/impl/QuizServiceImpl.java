package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.repository.CourseRepository;
import ru.mifi.ormplatform.repository.ModuleRepository;
import ru.mifi.ormplatform.repository.QuizRepository;
import ru.mifi.ormplatform.service.QuizService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с квизами (тестами) по модулям курса.
 */
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param quizRepository   репозиторий квизов.
     * @param courseRepository репозиторий курсов.
     * @param moduleRepository репозиторий модулей.
     */
    public QuizServiceImpl(QuizRepository quizRepository,
                           CourseRepository courseRepository,
                           ModuleRepository moduleRepository) {
        this.quizRepository = quizRepository;
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public Quiz createQuiz(Long courseId,
                           Long moduleId,
                           String title,
                           Integer timeLimitMinutes) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + courseId + " не найден"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Модуль с id=" + moduleId + " не найден"));

        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setModule(module);
        quiz.setTitle(title);
        quiz.setTimeLimit(timeLimitMinutes);

        return quizRepository.save(quiz);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quiz> findByModule(Long moduleId) {
        return quizRepository.findByModule_Id(moduleId).stream().findFirst();
        // если у вас в репозитории метод Optional<Quiz> findByModule_Id(...) —
        // здесь можно просто вернуть его
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quiz> findByCourse(Long courseId) {
        return quizRepository.findAllByCourse_Id(courseId);
    }
}

