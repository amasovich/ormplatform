package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.web.dto.AnswerOptionDto;
import ru.mifi.ormplatform.web.dto.QuestionDto;
import ru.mifi.ormplatform.web.dto.QuizDto;
import ru.mifi.ormplatform.web.dto.QuizSubmissionDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер между сущностями квизов и DTO.
 */
@Component
public class QuizMapper {

    /**
     * Преобразую сущность Quiz в DTO с вложенными вопросами и вариантами.
     *
     * @param quiz сущность квиза
     * @return DTO квиза для REST-слоя
     */
    public QuizDto toQuizDto(Quiz quiz) {
        if (quiz == null) {
            return null;
        }

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

        if (quiz.getQuestions() != null) {
            List<QuestionDto> questionDtos = quiz.getQuestions()
                    .stream()
                    .map(this::toQuestionDto)
                    .collect(Collectors.toList());
            dto.setQuestions(questionDtos);
        }

        return dto;
    }

    /**
     * Маппинг вопроса квиза.
     *
     * @param question сущность вопроса
     * @return DTO вопроса
     */
    public QuestionDto toQuestionDto(Question question) {
        if (question == null) {
            return null;
        }

        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setType(question.getType() != null ? question.getType().name() : null);

        if (question.getOptions() != null) {
            List<AnswerOptionDto> options = question.getOptions()
                    .stream()
                    .map(this::toAnswerOptionDto)
                    .collect(Collectors.toList());
            dto.setOptions(options);
        }

        return dto;
    }

    /**
     * Маппинг одного варианта ответа.
     *
     * @param answerOption сущность варианта
     * @return DTO варианта ответа
     */
    public AnswerOptionDto toAnswerOptionDto(AnswerOption answerOption) {
        if (answerOption == null) {
            return null;
        }

        AnswerOptionDto dto = new AnswerOptionDto();
        dto.setId(answerOption.getId());
        dto.setText(answerOption.getText());
        return dto;
    }

    /**
     * Маппинг результата прохождения квиза.
     *
     * @param submission сущность результата
     * @return DTO результата
     */
    public QuizSubmissionDto toQuizSubmissionDto(QuizSubmission submission) {
        if (submission == null) {
            return null;
        }

        QuizSubmissionDto dto = new QuizSubmissionDto();
        dto.setId(submission.getId());

        if (submission.getQuiz() != null) {
            dto.setQuizId(submission.getQuiz().getId());
            dto.setQuizTitle(submission.getQuiz().getTitle());
        }

        if (submission.getStudent() != null) {
            dto.setStudentId(submission.getStudent().getId());
            dto.setStudentName(submission.getStudent().getName());
        }

        dto.setScore(submission.getScore());
        dto.setTakenAt(submission.getTakenAt());

        return dto;
    }
}
