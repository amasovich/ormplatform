package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.web.dto.AnswerOptionDto;
import ru.mifi.ormplatform.web.dto.QuestionDto;
import ru.mifi.ormplatform.web.dto.QuizDto;
import ru.mifi.ormplatform.web.dto.QuizSubmissionResponseDto;
import ru.mifi.ormplatform.web.dto.QuizSummaryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для квизов и их результатов.
 */
@Component
public class QuizMapper {

    /**
     * Краткое представление квиза для списков по курсу/модулю.
     */
    public QuizSummaryDto toSummaryDto(Quiz quiz) {
        QuizSummaryDto dto = new QuizSummaryDto();
        dto.setId(quiz.getId());
        if (quiz.getCourse() != null) {
            dto.setCourseId(quiz.getCourse().getId());
        }
        if (quiz.getModule() != null) {
            dto.setModuleId(quiz.getModule().getId());
        }
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());

        if (quiz.getQuestions() != null) {
            dto.setQuestionCount(quiz.getQuestions().size());

            int maxScore = quiz.getQuestions().stream()
                    .filter(q -> q.getOptions() != null)
                    .mapToInt(q -> (int) q.getOptions().stream()
                            .filter(AnswerOption::isCorrect)
                            .count())
                    .sum();
            dto.setMaxScore(maxScore);
        } else {
            dto.setQuestionCount(0);
            dto.setMaxScore(0);
        }

        return dto;
    }

    public List<QuizSummaryDto> toSummaryList(List<Quiz> quizzes) {
        if (quizzes == null) {
            return List.of();
        }
        return quizzes.stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Полное представление квиза с вопросами и вариантами.
     */
    public QuizDto toQuizDto(Quiz quiz) {
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());

        if (quiz.getCourse() != null) {
            dto.setCourseId(quiz.getCourse().getId());
        }
        if (quiz.getModule() != null) {
            dto.setModuleId(quiz.getModule().getId());
        }

        List<QuestionDto> questionDtos = new ArrayList<>();
        if (quiz.getQuestions() != null) {
            for (Question question : quiz.getQuestions()) {
                questionDtos.add(toQuestionDto(question));
            }
        }
        dto.setQuestions(questionDtos);

        return dto;
    }

    private QuestionDto toQuestionDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setText(question.getText());
        // В DTO тип обычно строкой (SINGLE_CHOICE / MULTIPLE_CHOICE / TEXT)
        dto.setType(question.getType() != null ? question.getType().name() : null);

        List<AnswerOptionDto> optionDtos = new ArrayList<>();
        if (question.getOptions() != null) {
            for (AnswerOption option : question.getOptions()) {
                optionDtos.add(toAnswerOptionDto(option));
            }
        }
        dto.setOptions(optionDtos);

        return dto;
    }

    private AnswerOptionDto toAnswerOptionDto(AnswerOption option) {
        AnswerOptionDto dto = new AnswerOptionDto();
        dto.setId(option.getId());
        dto.setText(option.getText());
        // В DTO НЕ передаём флаг правильности, чтобы не раскрывать ответы студенту.
        // Поэтому здесь НИКАКИХ вызовов setCorrect(...) быть не должно.
        return dto;
    }

    /**
     * Маппинг результата прохождения квиза.
     */
    public QuizSubmissionResponseDto toSubmissionResponseDto(QuizSubmission submission) {
        QuizSubmissionResponseDto dto = new QuizSubmissionResponseDto();
        dto.setId(submission.getId());
        dto.setQuizId(submission.getQuiz().getId());
        dto.setQuizTitle(submission.getQuiz().getTitle());
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getName());
        dto.setScore(submission.getScore());
        dto.setTakenAt(submission.getTakenAt());
        return dto;
    }

    public List<QuizSubmissionResponseDto> toSubmissionResponseList(List<QuizSubmission> submissions) {
        if (submissions == null) {
            return List.of();
        }
        return submissions.stream()
                .map(this::toSubmissionResponseDto)
                .collect(Collectors.toList());
    }
}
