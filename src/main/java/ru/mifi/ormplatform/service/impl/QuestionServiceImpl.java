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

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст вопроса не может быть пустым");
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Квиз с id=" + quizId + " не найден"));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(text.trim());
        question.setType(type);

        return questionRepository.save(question);
    }

    @Override
    public AnswerOption addAnswerOption(Long questionId,
                                        String text,
                                        boolean isCorrect) {

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст варианта ответа не может быть пустым");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Вопрос с id=" + questionId + " не найден"));

        // Нельзя добавлять варианты к TEXT-вопросу
        if (question.getType() == QuestionType.TEXT) {
            throw new IllegalStateException(
                    "К TEXT-вопросу нельзя добавлять варианты ответа");
        }

        // Проверка, что SINGLE_CHOICE может иметь только 1 правильный вариант
        if (question.getType() == QuestionType.SINGLE_CHOICE && isCorrect) {
            long correctCount = answerOptionRepository.findAllByQuestion_Id(questionId)
                    .stream()
                    .filter(AnswerOption::isCorrect)
                    .count();

            if (correctCount >= 1) {
                throw new IllegalStateException(
                        "SINGLE_CHOICE вопрос может иметь только один правильный ответ");
            }
        }

        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setText(text.trim());
        option.setCorrect(isCorrect);

        return answerOptionRepository.save(option);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findByQuiz(Long quizId) {
        return questionRepository.findAllByQuiz_Id(quizId);
    }
}
