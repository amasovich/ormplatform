package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Lesson;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с уроками внутри модулей курса.
 * Предоставляет операции создания, чтения, обновления и удаления уроков.
 */
public interface LessonService {

    /**
     * Создаю новый урок внутри указанного модуля.
     *
     * @param moduleId идентификатор модуля, которому принадлежит урок.
     * @param title    название урока.
     * @param content  содержимое урока (обязательное поле).
     * @param videoUrl ссылка на видеоурок (может быть null).
     * @return созданный урок.
     */
    Lesson createLesson(Long moduleId,
                        String title,
                        String content,
                        String videoUrl);

    /**
     * Обновляю параметры урока.
     *
     * @param id       идентификатор урока.
     * @param title    новое название (опционально).
     * @param content  новое содержимое (опционально).
     * @param videoUrl новая ссылка на видео (опционально).
     * @return обновлённый урок.
     */
    Lesson updateLesson(Long id,
                        String title,
                        String content,
                        String videoUrl);

    /**
     * Удаляю урок.
     *
     * @param id идентификатор удаляемого урока.
     */
    void deleteLesson(Long id);

    /**
     * Получаю урок по ID.
     *
     * @param id идентификатор урока.
     * @return Optional с уроком.
     */
    Optional<Lesson> findById(Long id);

    /**
     * Получаю все уроки конкретного модуля.
     *
     * @param moduleId идентификатор модуля.
     * @return список уроков.
     */
    List<Lesson> findByModule(Long moduleId);
}
