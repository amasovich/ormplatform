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

        // Проверяем, что модуль принадлежит курсу
        if (!module.getCourse().getId().equals(courseId)) {
            throw new IllegalArgumentException(
                    "Модуль id=" + moduleId + " не принадлежит курсу id=" + courseId);
        }

        // Проверяем, что у модуля нет существующего квиза
        if (quizRepository.findByModule_Id(moduleId).isPresent()) {
            throw new IllegalStateException(
                    "Квиз для модуля id=" + moduleId + " уже существует");
        }

        Quiz quiz = new Quiz();
        quiz.setModule(module);
        quiz.setTitle(title.trim());
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
        return quizRepository.findByModule_Id(moduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quiz> findByCourse(Long courseId) {
        // 🔥 корректный способ — через модули
        List<Module> modules =
                moduleRepository.findAllByCourse_IdOrderByOrderIndexAsc(courseId);

        return modules.stream()
                .map(module -> quizRepository.findByModule_Id(module.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Quiz updateQuiz(Long id, String title, Integer timeLimitMinutes) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Квиз с id=" + id + " не найден"));

        quiz.setTitle(title);
        quiz.setTimeLimit(timeLimitMinutes);

        return quizRepository.save(quiz);
    }

    @Override
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new IllegalArgumentException("Квиз с id=" + id + " не найден");
        }
        quizRepository.deleteById(id);
    }

}
