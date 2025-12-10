package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.enums.QuestionType;
import ru.mifi.ormplatform.repository.AnswerOptionRepository;
import ru.mifi.ormplatform.repository.QuestionRepository;
import ru.mifi.ormplatform.repository.QuizRepository;
import ru.mifi.ormplatform.service.QuestionService;

import java.util.List;

/**
 * Реализация сервиса для работы с вопросами квиза и вариантами ответов.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizRepository quizRepository;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param questionRepository     репозиторий вопросов.
     * @param answerOptionRepository репозиторий вариантов ответов.
     * @param quizRepository         репозиторий квизов.
     */
    public QuestionServiceImpl(QuestionRepository questionRepository,
                               AnswerOptionRepository answerOptionRepository,
                               QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Question createQuestion(Long quizId,
                                   String text,
                                   QuestionType type) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Квиз с id=" + quizId + " не найден"));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(text);
        question.setType(type);

        return questionRepository.save(question);
    }

    @Override
    public AnswerOption addAnswerOption(Long questionId,
                                        String text,
                                        boolean isCorrect) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Вопрос с id=" + questionId + " не найден"));

        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setText(text);
        option.setCorrect(isCorrect);

        return answerOptionRepository.save(option);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findByQuiz(Long quizId) {
        return questionRepository.findAllByQuiz_Id(quizId);
    }
}

