package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.web.dto.*;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class QuizMapper {

    /**
     * Полное описание квиза со списком вопросов.
     */
    public QuizDto toQuizDto(Quiz quiz) {
        QuizDto dto = new QuizDto();

        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());

        if (quiz.getModule() != null) {
            dto.setModuleId(quiz.getModule().getId());

            if (quiz.getModule().getCourse() != null) {
                dto.setCourseId(quiz.getModule().getCourse().getId());
            }
        }

        dto.setQuestions(
                quiz.getQuestions() == null
                        ? Collections.emptyList()
                        : quiz.getQuestions().stream()
                        .map(this::toQuestionDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    /**
     * Краткое описание квиза для списков.
     */
    public QuizSummaryDto toQuizSummaryDto(Quiz quiz) {
        QuizSummaryDto dto = new QuizSummaryDto();

        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimit(quiz.getTimeLimit());

        if (quiz.getModule() != null) {
            dto.setModuleId(quiz.getModule().getId());

            if (quiz.getModule().getCourse() != null) {
                dto.setCourseId(quiz.getModule().getCourse().getId());
            }
        }

        int questionCount = quiz.getQuestions() == null ? 0 : quiz.getQuestions().size();
        dto.setQuestionCount(questionCount);

        int maxScore = quiz.getQuestions() == null ? 0 :
                (int) quiz.getQuestions().stream()
                        .flatMap(q ->
                                q.getOptions() == null ? Collections.<AnswerOption>emptyList().stream()
                                        : q.getOptions().stream()
                        )
                        .filter(AnswerOption::isCorrect)
                        .count();

        dto.setMaxScore(maxScore);

        return dto;
    }

    /**
     * Результат прохождения квиза.
     */
    public QuizSubmissionDto toQuizSubmissionDto(QuizSubmission submission) {
        QuizSubmissionDto dto = new QuizSubmissionDto();

        dto.setId(submission.getId());
        dto.setQuizId(submission.getQuiz().getId());
        dto.setQuizTitle(submission.getQuiz().getTitle());
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getName());
        dto.setScore(submission.getScore());
        dto.setTakenAt(submission.getTakenAt());

        return dto;
    }

    /**
     * Вопрос квиза.
     */
    public QuestionDto toQuestionDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setType(question.getType()); // Экспортируем enum прямо

        dto.setOptions(
                question.getOptions().stream()
                        .map(this::toAnswerOptionDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    /**
     * Вариант ответа.
     */
    public AnswerOptionDto toAnswerOptionDto(AnswerOption option) {
        AnswerOptionDto dto = new AnswerOptionDto();

        dto.setId(option.getId());
        dto.setText(option.getText());
        dto.setCorrect(option.isCorrect());

        return dto;
    }
}
