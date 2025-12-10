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

    AnswerOption addAnswerOption(Long questionId,
                                 String text,
                                 boolean isCorrect);

    List<Question> findByQuiz(Long quizId);
}

