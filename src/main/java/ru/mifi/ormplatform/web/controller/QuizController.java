package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.AnswerOption;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.service.QuizService;
import ru.mifi.ormplatform.service.QuizSubmissionService;
import ru.mifi.ormplatform.web.dto.QuizDto;
import ru.mifi.ormplatform.web.dto.QuizSubmissionRequestDto;
import ru.mifi.ormplatform.web.dto.QuizSubmissionResponseDto;
import ru.mifi.ormplatform.web.dto.QuizSummaryDto;
import ru.mifi.ormplatform.web.mapper.QuizMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для работы с квизами и результатами.
 */
@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;
    private final QuizSubmissionService quizSubmissionService;
    private final QuizMapper quizMapper;

    public QuizController(QuizService quizService,
                          QuizSubmissionService quizSubmissionService,
                          QuizMapper quizMapper) {
        this.quizService = quizService;
        this.quizSubmissionService = quizSubmissionService;
        this.quizMapper = quizMapper;
    }

    // ---------------------------------------------------------
    // 1. Списки и получение квизов
    // ---------------------------------------------------------

    /**
     * Список квизов по курсу (краткое представление).
     *
     * GET /api/courses/{courseId}/quizzes
     */
    @GetMapping("/courses/{courseId}/quizzes")
    public List<QuizSummaryDto> getQuizzesByCourse(@PathVariable Long courseId) {
        List<Quiz> quizzes = quizService.findByCourse(courseId);
        return quizMapper.toSummaryList(quizzes);
    }

    /**
     * Квиз, привязанный к модулю (если он там один).
     *
     * GET /api/modules/{moduleId}/quiz
     */
    @GetMapping("/modules/{moduleId}/quiz")
    public ResponseEntity<QuizDto> getQuizByModule(@PathVariable Long moduleId) {
        return quizService.findByModule(moduleId)
                .map(quizMapper::toQuizDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Полный квиз по id (с вопросами и вариантами).
     *
     * GET /api/quizzes/{quizId}
     */
    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long quizId) {
        return quizService.findById(quizId)
                .map(quizMapper::toQuizDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------
    // 2. Отправка и просмотр результатов
    // ---------------------------------------------------------

    /**
     * Отправить ответы на квиз и получить результат.
     *
     * POST /api/quizzes/{quizId}/submissions
     *
     * Пример тела запроса:
     * {
     *   "studentId": 2,
     *   "answers": {
     *     "1": 1,
     *     "2": 4
     *   }
     * }
     */
    @PostMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<QuizSubmissionResponseDto> submitQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizSubmissionRequestDto request) {

        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + quizId));

        int score = calculateScore(quiz, request);

        QuizSubmission submission = quizSubmissionService.createSubmission(
                quizId,
                request.getStudentId(),
