package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Lesson;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с уроками внутри модуля.
 */
public interface LessonService {

    /**
     * Создаю новый урок в модуле.
     *
     * @param moduleId идентификатор модуля.
     * @param title    название урока.
     * @param content  текстовое содержимое (описание, конспект).
     * @param videoUrl ссылка на видеоурок (может быть null).
     * @return созданный урок.
     */
    Lesson createLesson(Long moduleId,
                        String title,
                        String content,
                        String videoUrl);

    /**
     * Обновляю урок.
     */
    Lesson updateLesson(Long id,
                        String title,
                        String content,
                        String videoUrl);

    /**
     * Удаляю урок по id.
     */
    void deleteLesson(Long id);

    /**
     * Получаю урок по идентификатору.
     */
    Optional<Lesson> findById(Long id);

    /**
     * Получаю список всех уроков конкретного модуля.
     */
    List<Lesson> findByModule(Long moduleId);
}

