package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
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
 * Реализация сервиса вопросов и вариантов ответа.
 * Выполняет валидацию типов вопросов, предотвращает некорректные комбинации
 * и обеспечивает целостность данных квиза.
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

    // ============================================================================
    // CREATE QUESTION
    // ============================================================================
    @Override
    public Question createQuestion(Long quizId, String text, QuestionType type) {

        if (text == null || text.isBlank()) {
            throw new ValidationException("Question text cannot be empty");
        }
        if (type == null) {
            throw new ValidationException("Question type cannot be null");
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Quiz not found: id=" + quizId));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(text.trim());
        question.setType(type);

        return questionRepository.save(question);
    }

    // ============================================================================
    // UPDATE QUESTION
    // ============================================================================
    @Override
    public Question updateQuestion(Long questionId,
                                   String newText,
                                   QuestionType newType) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Question not found: id=" + questionId));

        // обновление текста
        if (newText != null) {
            String normalized = newText.trim();
            if (normalized.isEmpty()) {
                throw new ValidationException("Question text cannot be empty");
            }
            question.setText(normalized);
        }

        // смена типа
        if (newType != null && newType != question.getType()) {

            List<AnswerOption> existingOptions =
                    answerOptionRepository.findAllByQuestion_Id(questionId);

            // TEXT-вопросы не могут иметь варианты
            if (newType == QuestionType.TEXT && !existingOptions.isEmpty()) {
                throw new ValidationException(
                        "Cannot change question to TEXT: it already contains answer options");
            }

            // SINGLE_CHOICE → нельзя назначить >1 правильного
            if (newType == QuestionType.SINGLE_CHOICE) {
                long correctCount = existingOptions.stream()
                        .filter(AnswerOption::isCorrect)
                        .count();

                if (correctCount > 1) {
                    throw new ValidationException(
                            "Cannot change to SINGLE_CHOICE: question already has multiple correct answers");
                }
            }

            question.setType(newType);
        }

        return questionRepository.save(question);
    }

    // ============================================================================
    // DELETE QUESTION
    // ============================================================================
    @Override
    public void deleteQuestion(Long questionId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Question not found: id=" + questionId));

        // удаляем варианты ответа
        List<AnswerOption> options = answerOptionRepository.findAllByQuestion_Id(questionId);
        answerOptionRepository.deleteAll(options);

        questionRepository.delete(question);
    }

    // ============================================================================
    // ADD ANSWER OPTION
    // ============================================================================
    @Override
    public AnswerOption addAnswerOption(Long questionId,
                                        String text,
                                        boolean isCorrect) {

        if (text == null || text.isBlank()) {
            throw new ValidationException("Answer option text cannot be empty");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Question not found: id=" + questionId));

        // TEXT-вопросы не имеют вариантов
        if (question.getType() == QuestionType.TEXT) {
            throw new ValidationException(
                    "Cannot add answer options to TEXT question");
        }

        // SINGLE_CHOICE может иметь только 1 правильный
        if (question.getType() == QuestionType.SINGLE_CHOICE && isCorrect) {

            boolean alreadyHasCorrect =
                    answerOptionRepository.findAllByQuestion_Id(questionId)
                            .stream()
                            .anyMatch(AnswerOption::isCorrect);

            if (alreadyHasCorrect) {
                throw new ValidationException(
                        "SINGLE_CHOICE question already has a correct option");
            }
        }

        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setText(text.trim());
        option.setCorrect(isCorrect);

        return answerOptionRepository.save(option);
    }

    // ============================================================================
    // UPDATE ANSWER OPTION
    // ============================================================================
    @Override
    public AnswerOption updateAnswerOption(Long optionId,
                                           String text,
                                           boolean isCorrect) {

        AnswerOption option = answerOptionRepository.findById(optionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Answer option not found: id=" + optionId));

        if (text != null) {
            String normalized = text.trim();
            if (normalized.isEmpty()) {
                throw new ValidationException("Answer option text cannot be empty");
            }
            option.setText(normalized);
        }

        Question question = option.getQuestion();

        // SINGLE_CHOICE → только один корректный
        if (question.getType() == QuestionType.SINGLE_CHOICE && isCorrect) {
            boolean existsOtherCorrect =
                    answerOptionRepository.findAllByQuestion_Id(question.getId())
                            .stream()
                            .anyMatch(o -> o.isCorrect() && !o.getId().equals(optionId));

            if (existsOtherCorrect) {
                throw new ValidationException(
                        "SINGLE_CHOICE question cannot have more than one correct option");
            }
        }

        option.setCorrect(isCorrect);

        return answerOptionRepository.save(option);
    }

    // ============================================================================
    // DELETE ANSWER OPTION
    // ============================================================================
    @Override
    public void deleteAnswerOption(Long optionId) {

        AnswerOption option = answerOptionRepository.findById(optionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Answer option not found: id=" + optionId));

        answerOptionRepository.delete(option);
    }

    // ============================================================================
    // FIND QUESTIONS BY QUIZ
    // ============================================================================
    @Override
    @Transactional(readOnly = true)
    public List<Question> findByQuiz(Long quizId) {
        return questionRepository.findAllByQuiz_Id(quizId);
    }
}
