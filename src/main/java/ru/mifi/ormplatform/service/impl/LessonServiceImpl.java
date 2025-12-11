package ru.mifi.ormplatform.service.impl;

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
 * Реализация сервиса уроков.
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

    @Override
    public Lesson createLesson(Long moduleId,
                               String title,
                               String content,
                               String videoUrl) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Модуль с id=" + moduleId + " не найден"));

        // Нормализация
        String normalizedTitle = title.trim();
        String normalizedContent = content != null ? content.trim() : null;
        String normalizedVideo = videoUrl != null ? videoUrl.trim() : null;

        // Валидация
        if (normalizedTitle.isEmpty()) {
            throw new IllegalArgumentException("Название урока не может быть пустым");
        }
        if (normalizedContent == null || normalizedContent.isEmpty()) {
            throw new IllegalArgumentException("Содержимое урока не может быть пустым");
        }

        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle(normalizedTitle);
        lesson.setContent(normalizedContent);
        lesson.setVideoUrl(normalizedVideo);

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(Long id,
                               String title,
                               String content,
                               String videoUrl) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Урок с id=" + id + " не найден"));

        if (title != null) lesson.setTitle(title.trim());
        if (content != null) lesson.setContent(content.trim());
        lesson.setVideoUrl(videoUrl);

        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Урок с id=" + id + " не найден"));

        lessonRepository.delete(lesson);
    }

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

