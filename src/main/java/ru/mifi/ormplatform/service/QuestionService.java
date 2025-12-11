package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.enums.QuestionType;

import java.util.List;

/**
 * Сервис для работы с вопросами квизов и вариантами ответов.
 * <p>
 * Поддерживает создание, обновление и удаление вопросов,
 * а также управление вариантами ответов (добавление / изменение / удаление).
 * Гарантирует корректность логики типов вопросов:
 * <ul>
 *     <li>TEXT — не допускает вариантов ответа;</li>
 *     <li>SINGLE_CHOICE — допускает только один правильный вариант;</li>
 *     <li>MULTI_CHOICE — допускает произвольное количество правильных вариантов.</li>
 * </ul>
 */
public interface QuestionService {

    /**
     * Создаю новый вопрос для указанного квиза.
     *
     * @param quizId идентификатор квиза.
     * @param text текст вопроса.
     * @param type тип вопроса (TEXT, SINGLE_CHOICE, MULTI_CHOICE).
     * @return созданный вопрос.
     *
     * @throws jakarta.validation.ValidationException если текст пустой или тип null.
     * @throws jakarta.persistence.EntityNotFoundException если квиз не найден.
     */
    Question createQuestion(Long quizId,
                            String text,
                            QuestionType type);

    /**
     * Обновляю текст или тип вопроса.
     * <p>
     * При смене типа выполняются проверки:
     * <ul>
     *     <li>нельзя сменить на TEXT, если у вопроса уже есть варианты;</li>
     *     <li>нельзя сменить на SINGLE_CHOICE, если уже существует более одного правильного варианта;</li>
     * </ul>
     *
     * @param questionId идентификатор вопроса.
     * @param newText новый текст вопроса (null — не менять).
     * @param newType новый тип вопроса (null — не менять).
     * @return обновлённый вопрос.
     *
     * @throws jakarta.validation.ValidationException при нарушении бизнес-правил.
     * @throws jakarta.persistence.EntityNotFoundException если вопрос не найден.
     */
    Question updateQuestion(Long questionId,
                            String newText,
                            QuestionType newType);

    /**
     * Удаляю вопрос вместе со всеми связанными вариантами ответов.
     *
     * @param questionId идентификатор вопроса.
     *
     * @throws jakarta.persistence.EntityNotFoundException если вопрос не найден.
     */
    void deleteQuestion(Long questionId);

    /**
     * Добавляю новый вариант ответа к вопросу.
     * <p>
     * Бизнес-правила:
     * <ul>
     *     <li>TEXT-вопросы не могут иметь варианты;</li>
     *     <li>для SINGLE_CHOICE допускается не более одного правильного варианта;</li>
     *     <li>для MULTI_CHOICE ограничений по правильным ответам нет.</li>
     * </ul>
     *
     * @param questionId идентификатор вопроса.
     * @param text текст варианта ответа.
     * @param isCorrect является ли вариант правильным.
     * @return созданный вариант ответа.
     *
     * @throws jakarta.validation.ValidationException при нарушении правил.
     * @throws jakarta.persistence.EntityNotFoundException если вопрос не найден.
     */
    AnswerOption addAnswerOption(Long questionId,
                                 String text,
                                 boolean isCorrect);

    /**
     * Обновляю вариант ответа.
     * <p>
     * Если вариант помечается как правильный у SINGLE_CHOICE,
     * выполняется проверка, что другой правильный вариант отсутствует.
     *
     * @param optionId идентификатор варианта.
     * @param text новый текст варианта (null — не менять).
     * @param isCorrect новое значение флага «правильный».
     * @return обновлённый вариант ответа.
     *
     * @throws jakarta.validation.ValidationException при нарушении бизнес-логики.
     * @throws jakarta.persistence.EntityNotFoundException если вариант ответа не найден.
     */
    AnswerOption updateAnswerOption(Long optionId,
                                    String text,
                                    boolean isCorrect);

    /**
     * Удаляю вариант ответа.
     *
     * @param optionId идентификатор варианта ответа.
     *
     * @throws jakarta.persistence.EntityNotFoundException если вариант не найден.
     */
    void deleteAnswerOption(Long optionId);

    /**
     * Возвращаю все вопросы для указанного квиза.
     *
     * @param quizId идентификатор квиза.
     * @return список вопросов.
     */
    List<Question> findByQuiz(Long quizId);
}
