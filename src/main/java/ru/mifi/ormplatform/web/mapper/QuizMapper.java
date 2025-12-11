package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.web.dto.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования сущностей квизов:
 * {@link Quiz}, {@link Question}, {@link AnswerOption}, {@link QuizSubmission}
 * в соответствующие DTO-модели.
 * <p>
 * Каждый метод null-safe и не выбрасывает NullPointerException.
 */
@Component
public class QuizMapper {

    // -------------------------------------------------------------------------
    // QUIZ → QuizDto (полное описание)
    // -------------------------------------------------------------------------

    /**
     * Преобразует сущность {@link Quiz} в DTO {@link QuizDto}
     * со списком вопросов и вариантов ответов.
     *
     * @param quiz исходная сущность.
     * @return DTO или null.
     */
    public QuizDto toQuizDto(Quiz quiz) {
        if (quiz == null) return null;

        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());

        // Module & Course
        if (quiz.getModule() != null) {
            dto.setModuleId(quiz.getModule().getId());
            if (quiz.getModule().getCourse() != null) {
                dto.setCourseId(quiz.getModule().getCourse().getId());
            }
        }

        // Questions
        List<Question> questions = quiz.getQuestions();
        dto.setQuestions(
                questions == null
                        ? Collections.emptyList()
                        : questions.stream()
                        .map(this::toQuestionDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    // -------------------------------------------------------------------------
    // QUIZ → QuizSummaryDto (краткое описание)
    // -------------------------------------------------------------------------

    /**
     * Преобразует сущность {@link Quiz} в краткое DTO {@link QuizSummaryDto}.
     *
     * @param quiz квиз.
     * @return DTO или null.
     */
    public QuizSummaryDto toQuizSummaryDto(Quiz quiz) {
        if (quiz == null) return null;

        QuizSummaryDto dto = new QuizSummaryDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());

        // Module & Course
        if (quiz.getModule() != null) {
            dto.setModuleId(quiz.getModule().getId());
            if (quiz.getModule().getCourse() != null) {
                dto.setCourseId(quiz.getModule().getCourse().getId());
            }
        }

        // Количество вопросов
        List<Question> questions = quiz.getQuestions();
        dto.setQuestionCount(questions == null ? 0 : questions.size());

        // Максимальный балл — количество правильных ответов
        int maxScore = (questions == null) ? 0 :
                (int) questions.stream()
                        .flatMap(q ->
                                q.getOptions() == null
                                        ? Collections.<AnswerOption>emptyList().stream()
                                        : q.getOptions().stream()
                        )
                        .filter(AnswerOption::isCorrect)
                        .count();

        dto.setMaxScore(maxScore);

        return dto;
    }

    // -------------------------------------------------------------------------
    // SUBMISSION → QuizSubmissionDto
    // -------------------------------------------------------------------------

    /**
     * Преобразует отправку квиза в DTO результата.
     *
     * @param submission отправка.
     * @return DTO или null.
     */
    public QuizSubmissionDto toQuizSubmissionDto(QuizSubmission submission) {
        if (submission == null) return null;

        QuizSubmissionDto dto = new QuizSubmissionDto();

        dto.setId(submission.getId());
        dto.setScore(submission.getScore());
        dto.setTakenAt(submission.getTakenAt());

        if (submission.getQuiz() != null) {
            dto.setQuizId(submission.getQuiz().getId());
            dto.setQuizTitle(submission.getQuiz().getTitle());
        }

        if (submission.getStudent() != null) {
            dto.setStudentId(submission.getStudent().getId());
            dto.setStudentName(submission.getStudent().getName());
        }

        return dto;
    }

    // -------------------------------------------------------------------------
    // QUESTION → QuestionDto
    // -------------------------------------------------------------------------

    /**
     * Преобразует вопрос квиза в DTO с вариантами.
     *
     * @param question исходный вопрос.
     * @return DTO или null.
     */
    public QuestionDto toQuestionDto(Question question) {
        if (question == null) return null;

        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setType(question.getType());

        List<AnswerOption> options = question.getOptions();
        dto.setOptions(
                options == null
                        ? Collections.emptyList()
                        : options.stream()
                        .map(this::toAnswerOptionDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    // -------------------------------------------------------------------------
    // ANSWER OPTION → AnswerOptionDto
    // -------------------------------------------------------------------------

    /**
     * Преобразует вариант ответа в DTO.
     *
     * @param option вариант.
     * @return DTO или null.
     */
    public AnswerOptionDto toAnswerOptionDto(AnswerOption option) {
        if (option == null) return null;

        AnswerOptionDto dto = new AnswerOptionDto();
        dto.setId(option.getId());
        dto.setText(option.getText());
        dto.setCorrect(option.isCorrect());
        return dto;
    }
}
