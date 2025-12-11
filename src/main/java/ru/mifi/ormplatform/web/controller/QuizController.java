package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.service.QuestionService;
import ru.mifi.ormplatform.service.QuizService;
import ru.mifi.ormplatform.service.QuizSubmissionService;
import ru.mifi.ormplatform.web.dto.*;
import ru.mifi.ormplatform.web.mapper.QuizMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с квизами, вопросами и результатами прохождения.
 * <p>
 * Содержит:
 * – CRUD для квизов;
 * – CRUD для вопросов и вариантов ответов;
 * – отправку попытки прохождения квиза;
 * – получение результатов.
 */
@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final QuizSubmissionService quizSubmissionService;
    private final QuizMapper quizMapper;

    public QuizController(QuizService quizService,
                          QuestionService questionService,
                          QuizSubmissionService quizSubmissionService,
                          QuizMapper quizMapper) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.quizSubmissionService = quizSubmissionService;
        this.quizMapper = quizMapper;
    }

    /**
     * Создаю новый квиз внутри курса и модуля.
     *
     * POST /api/courses/{courseId}/modules/{moduleId}/quizzes
     */
    @PostMapping("/courses/{courseId}/modules/{moduleId}/quizzes")
    public ResponseEntity<QuizDto> createQuiz(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @Valid @RequestBody QuizCreateRequestDto request
    ) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Quiz title cannot be empty");
        }

        Quiz quiz = quizService.createQuiz(
                courseId,
                moduleId,
                request.getTitle(),
                request.getTimeLimit()
        );

        return ResponseEntity
                .created(URI.create("/api/quizzes/" + quiz.getId()))
                .body(quizMapper.toQuizDto(quiz));
    }

    /**
     * Обновляю существующий квиз.
     *
     * PUT /api/quizzes/{id}
     */
    @PutMapping("/quizzes/{id}")
    public ResponseEntity<QuizDto> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizUpdateRequestDto request
    ) {

        Quiz quiz = quizService.updateQuiz(
                id,
                request.getTitle(),
                request.getTimeLimit()
        );

        return ResponseEntity.ok(quizMapper.toQuizDto(quiz));
    }

    /**
     * Удаляю квиз.
     *
     * DELETE /api/quizzes/{id}
     */
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получаю список всех квизов курса.
     *
     * GET /api/courses/{courseId}/quizzes
     */
    @GetMapping("/courses/{courseId}/quizzes")
    public ResponseEntity<List<QuizSummaryDto>> getQuizzesByCourse(@PathVariable Long courseId) {

        List<QuizSummaryDto> dto = quizService.findByCourse(courseId)
                .stream()
                .map(quizMapper::toQuizSummaryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    /**
     * Получаю один квиз с вопросами и вариантами ответов.
     *
     * GET /api/quizzes/{quizId}
     */
    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable Long quizId) {

        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Quiz not found: id=" + quizId));

        return ResponseEntity.ok(quizMapper.toQuizDto(quiz));
    }

    /**
     * Добавляю вопрос в квиз.
     *
     * POST /api/quizzes/{quizId}/questions
     */
    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<QuestionDto> addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionCreateRequestDto request
    ) {

        if (request.getText() == null || request.getText().isBlank()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }

        Question question = questionService.createQuestion(
                quizId,
                request.getText(),
                request.getType()
        );

        return ResponseEntity.ok(quizMapper.toQuestionDto(question));
    }

    /**
     * Удалить вопрос из квиза.
     *
     * DELETE /api/questions/{id}
     */
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Добавляю вариант ответа к вопросу.
     *
     * POST /api/questions/{questionId}/options
     */
    @PostMapping("/questions/{questionId}/options")
    public ResponseEntity<AnswerOptionDto> addAnswerOption(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerOptionCreateRequestDto request
    ) {

        if (request.getText() == null || request.getText().isBlank()) {
            throw new IllegalArgumentException("Option text cannot be empty");
        }

        AnswerOption option = questionService.addAnswerOption(
                questionId,
                request.getText(),
                request.isCorrect()
        );

        return ResponseEntity.ok(quizMapper.toAnswerOptionDto(option));
    }

    /**
     * Удаляю вариант ответа.
     *
     * DELETE /api/answer-options/{id}
     */
    @DeleteMapping("/answer-options/{id}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable Long id) {
        questionService.deleteAnswerOption(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Студент отправляет ответы на квиз.
     * Подсчёт результата вынесен в сервис.
     *
     * POST /api/quizzes/{quizId}/submissions
     */
    @PostMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<QuizSubmissionDto> submitQuiz(
            @PathVariable Long quizId,
            @Valid @RequestBody QuizSubmissionRequestDto request
    ) {

        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("studentId is required");
        }
        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new IllegalArgumentException("answers list cannot be empty");
        }

        QuizSubmission submission = quizSubmissionService.evaluateAndSaveSubmission(
                quizId,
                request.getStudentId(),
                request.getAnswers(),
                LocalDateTime.now()
        );

        return ResponseEntity.ok(quizMapper.toQuizSubmissionDto(submission));
    }

    /**
     * Список всех результатов по квизу.
     *
     * GET /api/quizzes/{quizId}/submissions
     */
    @GetMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<List<QuizSubmissionDto>> getSubmissionsByQuiz(
            @PathVariable Long quizId) {

        List<QuizSubmissionDto> dto = quizSubmissionService.findByQuiz(quizId)
                .stream()
                .map(quizMapper::toQuizSubmissionDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    /**
     * Список всех результатов студента.
     *
     * GET /api/students/{studentId}/quiz-submissions
     */
    @GetMapping("/students/{studentId}/quiz-submissions")
    public ResponseEntity<List<QuizSubmissionDto>> getSubmissionsByStudent(
            @PathVariable Long studentId) {

        List<QuizSubmissionDto> dto = quizSubmissionService.findByStudent(studentId)
                .stream()
                .map(quizMapper::toQuizSubmissionDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }
}
