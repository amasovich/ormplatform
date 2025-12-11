package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.repository.LessonRepository;
import ru.mifi.ormplatform.repository.ModuleRepository;
import ru.mifi.ormplatform.service.LessonService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация LessonService.
 * Выполняет полную валидацию входных данных, нормализацию строковых полей
 * и обработку ошибок в соответствии с единым стилем платформы ORM.
 */
@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    public LessonServiceImpl(LessonRepository lessonRepository,
                             ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }

    // ============================================================================
    //                                CREATE LESSON
    // ============================================================================

    @Override
    public Lesson createLesson(Long moduleId,
                               String title,
                               String content,
                               String videoUrl) {

        // -----------------------------
        // Валидация входных данных
        // -----------------------------
        if (moduleId == null) {
            throw new ValidationException("moduleId is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Lesson title cannot be empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new ValidationException("Lesson content cannot be empty");
        }

        // -----------------------------
        // Получение модуля
        // -----------------------------
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Module not found: id=" + moduleId));

        // -----------------------------
        // Нормализация полей
        // -----------------------------
        String normalizedTitle = title.trim();
        String normalizedContent = content.trim();
        String normalizedVideo =
                (videoUrl != null && !videoUrl.trim().isEmpty())
                        ? videoUrl.trim()
                        : null;

        // -----------------------------
        // Создание урока
        // -----------------------------
        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(normalizedTitle);
        lesson.setContent(normalizedContent);
        lesson.setVideoUrl(normalizedVideo);

        return lessonRepository.save(lesson);
    }

    // ============================================================================
    //                                UPDATE LESSON
    // ============================================================================

    @Override
    public Lesson updateLesson(Long id,
                               String title,
                               String content,
                               String videoUrl) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Lesson not found: id=" + id));

        // Обновляем только переданные поля
        if (title != null) {
            if (title.trim().isEmpty()) {
                throw new ValidationException("Lesson title cannot be empty");
            }
            lesson.setTitle(title.trim());
        }

        if (content != null) {
            if (content.trim().isEmpty()) {
                throw new ValidationException("Lesson content cannot be empty");
            }
            lesson.setContent(content.trim());
        }

        if (videoUrl != null) {
            String normalizedVideo =
                    videoUrl.trim().isEmpty() ? null : videoUrl.trim();
            lesson.setVideoUrl(normalizedVideo);
        }

        return lessonRepository.save(lesson);
    }

    // ============================================================================
    //                                 DELETE LESSON
    // ============================================================================

    @Override
    public void deleteLesson(Long id) {

        if (!lessonRepository.existsById(id)) {
            throw new EntityNotFoundException("Lesson not found: id=" + id);
        }

        lessonRepository.deleteById(id);
    }

    // ============================================================================
    //                                    READ
    // ============================================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> findByModule(Long moduleId) {
        return lessonRepository.findAllByModule_Id(moduleId);
    }
}
