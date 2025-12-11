package ru.mifi.ormplatform.web.controller;

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

    // -----------------------------------------------------------------------
    //                               QUIZ CRUD
    // -----------------------------------------------------------------------

    /**
     * Создаю новый квиз внутри курса и модуля.
     *
     * POST /api/courses/{courseId}/modules/{moduleId}/quizzes
     */
    @PostMapping("/courses/{courseId}/modules/{moduleId}/quizzes")
    public ResponseEntity<QuizDto> createQuiz(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @RequestBody QuizCreateRequestDto request
    ) {

        Quiz quiz = quizService.createQuiz(
                courseId,
                moduleId,
                request.getTitle(),
                request.getTimeLimit()
        );

        return ResponseEntity.ok(quizMapper.toQuizDto(quiz));
    }

    /**
     * Обновляю существующий квиз.
     *
     * PUT /api/quizzes/{id}
     */
    @PutMapping("/quizzes/{id}")
    public ResponseEntity<QuizDto> updateQuiz(
            @PathVariable Long id,
            @RequestBody QuizUpdateRequestDto request
    ) {
        Quiz quiz = quizService.updateQuiz(id, request.getTitle(), request.getTimeLimit());
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

        List<Quiz> quizzes = quizService.findByCourse(courseId);

        List<QuizSummaryDto> dto = quizzes.stream()
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
                .orElseThrow(() -> new IllegalArgumentException("Квиз не найден: " + quizId));

        return ResponseEntity.ok(quizMapper.toQuizDto(quiz));
    }

    // -----------------------------------------------------------------------
    //            QUESTIONS — создание / удаление / добавление вариантов
    // -----------------------------------------------------------------------

    /**
     * Добавляю вопрос в квиз.
     *
     * POST /api/quizzes/{quizId}/questions
     */
    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<QuestionDto> addQuestion(
            @PathVariable Long quizId,
            @RequestBody QuestionCreateRequestDto request
    ) {

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

    // -----------------------------------------------------------------------
    //                       ANSWER OPTIONS — CRUD
    // -----------------------------------------------------------------------

    /**
     * Добавляю вариант ответа к вопросу.
     *
     * POST /api/questions/{questionId}/options
     */
    @PostMapping("/questions/{questionId}/options")
    public ResponseEntity<AnswerOptionDto> addAnswerOption(
            @PathVariable Long questionId,
            @RequestBody AnswerOptionCreateRequestDto request
    ) {

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

    // -----------------------------------------------------------------------
    //                        SUBMISSIONS (прохождение квиза)
    // -----------------------------------------------------------------------

    /**
     * Студент отправляет ответы на квиз.
     * Подсчёт результата вынесен в сервис.
     *
     * POST /api/quizzes/{quizId}/submissions
     */
    @PostMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<QuizSubmissionDto> submitQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizSubmissionRequestDto request
    ) {
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

        List<QuizSubmission> submissions = quizSubmissionService.findByQuiz(quizId);

        List<QuizSubmissionDto> dto = submissions.stream()
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

        List<QuizSubmission> submissions = quizSubmissionService.findByStudent(studentId);

        List<QuizSubmissionDto> dto = submissions.stream()
                .map(quizMapper::toQuizSubmissionDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }
}
