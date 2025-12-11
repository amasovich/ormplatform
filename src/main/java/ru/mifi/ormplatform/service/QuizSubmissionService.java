package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.QuizSubmission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с результатами прохождения квизов.
 * <p>
 * Поддерживает два сценария:
 * <ul>
 *     <li><b>createSubmission</b> — сохранение результата с уже подсчитанным score (ручной режим);</li>
 *     <li><b>evaluateAndSaveSubmission</b> — автоматическая проверка ответов и вычисление итогового балла.</li>
 * </ul>
 * Также предоставляет методы чтения результатов по студенту и по квизу.
 */
public interface QuizSubmissionService {

    /**
     * Создаю запись о прохождении квиза (без автоматической проверки ответов).
     * <p>
     * Используется, если оценка уже вычислена внешним компонентом.
     *
     * @param quizId    идентификатор квиза.
     * @param studentId идентификатор студента (User с ролью STUDENT).
     * @param score     итоговый балл (может быть null).
     * @param takenAt   время прохождения (если null — будет присвоено текущее).
     * @return созданная попытка QuizSubmission.
     *
     * @throws jakarta.persistence.EntityNotFoundException если квиз или пользователь не найдены.
     * @throws jakarta.validation.ValidationException если score отрицательный.
     * @throws IllegalStateException если студент уже проходил этот квиз.
     * @throws IllegalStateException если пользователь не является STUDENT.
     */
    QuizSubmission createSubmission(Long quizId,
                                    Long studentId,
                                    Integer score,
                                    LocalDateTime takenAt);

    /**
     * Автоматически проверяю ответы студента, вычисляю score
     * и сохраняю результат прохождения квиза.
     * <p>
     * Логика проверки:
     * <ul>
     *     <li>перебираются все вопросы квиза;</li>
     *     <li>из Map берётся выбранный вариант для каждого questionId;</li>
     *     <li>балл увеличивается, если выбранный вариант помечен как правильный;</li>
     *     <li>TEXT-вопросы игнорируются (в вашем домене не имеют вариантов).</li>
     * </ul>
     *
     * @param quizId    идентификатор квиза.
     * @param studentId идентификатор студента.
     * @param answers   Map вида questionId → answerOptionId.
     * @param takenAt   время прохождения (может быть null).
     * @return сохранённая запись QuizSubmission.
     *
     * @throws jakarta.persistence.EntityNotFoundException если квиз или пользователь не найдены.
     * @throws IllegalStateException если студент уже проходил этот квиз.
     * @throws IllegalStateException если пользователь не является STUDENT.
     */
    QuizSubmission evaluateAndSaveSubmission(Long quizId,
                                             Long studentId,
                                             Map<Long, Long> answers,
                                             LocalDateTime takenAt);

    /**
     * Получаю список всех прохождений для указанного квиза.
     *
     * @param quizId идентификатор квиза.
     * @return список QuizSubmission.
     */
    List<QuizSubmission> findByQuiz(Long quizId);

    /**
     * Получаю все попытки прохождения всех квизов указанным студентом.
     *
     * @param studentId идентификатор студента.
     * @return список QuizSubmission.
     */
    List<QuizSubmission> findByStudent(Long studentId);
}
