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

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    @Override
    public Question createQuestion(Long quizId, String text, QuestionType type) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст вопроса не может быть пустым");
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Квиз не найден: " + quizId));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(text.trim());
        question.setType(type);

        return questionRepository.save(question);
    }

    // -------------------------------------------------------------------------
    // UPDATE QUESTION
    // -------------------------------------------------------------------------
    @Override
    public Question updateQuestion(Long questionId, String newText, QuestionType newType) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Вопрос не найден: " + questionId));

        if (newText != null && !newText.trim().isEmpty()) {
            question.setText(newText.trim());
        }

        if (newType != null && question.getType() != newType) {

            // запрещаем смену типа на TEXT, если у вопроса сейчас есть варианты
            if (newType == QuestionType.TEXT &&
                    !answerOptionRepository.findAllByQuestion_Id(questionId).isEmpty()) {
                throw new IllegalStateException("Нельзя сменить вопрос на TEXT — у него есть варианты");
            }

            question.setType(newType);
        }

        return questionRepository.save(question);
    }

    // -------------------------------------------------------------------------
    // DELETE QUESTION
    // -------------------------------------------------------------------------
    @Override
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Вопрос не найден: " + questionId));

        // сначала удаляем все варианты ответа
        List<AnswerOption> options = answerOptionRepository.findAllByQuestion_Id(questionId);
        answerOptionRepository.deleteAll(options);

        questionRepository.delete(question);
    }

    // -------------------------------------------------------------------------
    // ADD ANSWER OPTION
    // -------------------------------------------------------------------------
    @Override
    public AnswerOption addAnswerOption(Long questionId, String text, boolean isCorrect) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст варианта не может быть пустым");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Вопрос не найден: " + questionId));

        if (question.getType() == QuestionType.TEXT) {
            throw new IllegalStateException("TEXT-вопрос не может иметь варианты ответа");
        }

        // единственный правильный ответ для SINGLE_CHOICE
        if (question.getType() == QuestionType.SINGLE_CHOICE && isCorrect) {
            boolean alreadyHasCorrect = answerOptionRepository.findAllByQuestion_Id(questionId)
                    .stream()
                    .anyMatch(AnswerOption::isCorrect);

            if (alreadyHasCorrect) {
                throw new IllegalStateException("SINGLE_CHOICE может иметь только один правильный вариант");
            }
        }

        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setText(text.trim());
        option.setCorrect(isCorrect);

        return answerOptionRepository.save(option);
    }

    // -------------------------------------------------------------------------
    // UPDATE ANSWER OPTION
    // -------------------------------------------------------------------------
    @Override
    public AnswerOption updateAnswerOption(Long optionId, String text, boolean isCorrect) {
        AnswerOption option = answerOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Вариант ответа не найден: " + optionId));

        if (text != null && !text.trim().isEmpty()) {
            option.setText(text.trim());
        }

        Question question = option.getQuestion();

        // проверяем корректность для SINGLE_CHOICE
        if (question.getType() == QuestionType.SINGLE_CHOICE && isCorrect) {
            boolean anotherCorrect = answerOptionRepository.findAllByQuestion_Id(question.getId())
                    .stream()
                    .anyMatch(o -> o.isCorrect() && !o.getId().equals(optionId));

            if (anotherCorrect) {
                throw new IllegalStateException("SINGLE_CHOICE может иметь только один правильный вариант");
            }
        }

        option.setCorrect(isCorrect);

        return answerOptionRepository.save(option);
    }

    // -------------------------------------------------------------------------
    // DELETE ANSWER OPTION
    // -------------------------------------------------------------------------
    @Override
    public void deleteAnswerOption(Long optionId) {
        AnswerOption option = answerOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Вариант ответа не найден: " + optionId));

        answerOptionRepository.delete(option);
    }

    // -------------------------------------------------------------------------
    // FIND QUESTIONS BY QUIZ
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<Question> findByQuiz(Long quizId) {
        return questionRepository.findAllByQuiz_Id(quizId);
    }
}
