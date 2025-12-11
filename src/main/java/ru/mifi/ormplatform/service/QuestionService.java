package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.enums.QuestionType;

import java.util.List;

/**
 * Сервис для работы с вопросами квиза и вариантами ответов.
 */
public interface QuestionService {

    Question createQuestion(Long quizId,
                            String text,
                            QuestionType type);

    Question updateQuestion(Long questionId,
                            String newText,
                            QuestionType newType);

    void deleteQuestion(Long questionId);

    AnswerOption addAnswerOption(Long questionId,
                                 String text,
                                 boolean isCorrect);

    AnswerOption updateAnswerOption(Long optionId,
                                    String text,
                                    boolean isCorrect);

    void deleteAnswerOption(Long optionId);

    List<Question> findByQuiz(Long quizId);
}
