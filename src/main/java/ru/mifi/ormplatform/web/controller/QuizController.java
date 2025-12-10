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
import ru.mifi.ormplatform.web.dto.QuizSubmissionDto;
import ru.mifi.ormplatform.web.dto.QuizSubmissionRequestDto;
import ru.mifi.ormplatform.web.dto.QuizSummaryDto;
import ru.mifi.ormplatform.web.mapper.QuizMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с квизами и отправкой ответов.
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

    /**
     * Получаю список квизов по курсу.
     * Здесь можно использовать либо QuizDto, либо ваш уже существующий QuizSummaryDto.
     *
     * @param courseId идентификатор курса
     * @return список квизов курса
     */
    @GetMapping("/courses/{courseId}/quizzes")
    public ResponseEntity<List<QuizDto>> getQuizzesByCourse(@PathVariable Long courseId) {
        List<Quiz> quizzes = quizService.findByCourse(courseId);

        List<QuizDto> result = quizzes.stream()
                .map(quizMapper::toQuizDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

//    @GetMapping("/courses/{courseId}/quizzes")
//    public List<QuizSummaryDto> getQuizzesForCourse(@PathVariable Long courseId) {
//        List<Quiz> quizzes = quizService.getQuizzesByCourse(courseId);
//        return quizMapper.toSummaryDtoList(quizzes);
//    }


    /**
     * Получаю один квиз с вопросами и вариантами ответов.
     *
     * @param quizId идентификатор квиза
     * @return детальное описание квиза
     */
    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable Long quizId) {
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Квиз не найден: " + quizId));

        return ResponseEntity.ok(quizMapper.toQuizDto(quiz));
    }

    /**
     * Отправка ответов студента на квиз.
     * Подсчёт score происходит на основе правильных вариантов в БД.
     *
     * @param quizId  идентификатор квиза
     * @param request ответы студента
     * @return сохранённый результат прохождения квиза
     */
    @PostMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<QuizSubmissionDto> submitQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizSubmissionRequestDto request
    ) {
        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Квиз не найден: " + quizId));

        int score = calculateScore(quiz, request.getAnswers());

        QuizSubmission submission = quizSubmissionService.createSubmission(
                quizId,
                request.getStudentId(),
                score,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(quizMapper.toQuizSubmissionDto(submission));
    }

    /**
     * Список результатов по конкретному квизу.
     *
     * @param quizId идентификатор квиза
     * @return список попыток прохождения этого квиза
     */
    @GetMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<List<QuizSubmissionDto>> getSubmissionsByQuiz(@PathVariable Long quizId) {
        List<QuizSubmission> submissions = quizSubmissionService.findByQuiz(quizId);

        List<QuizSubmissionDto> result = submissions.stream()
                .map(quizMapper::toQuizSubmissionDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Все результаты квизов для конкретного студента.
     *
     * @param studentId идентификатор студента
     * @return список всех попыток студента по квизам
     */
    @GetMapping("/students/{studentId}/quiz-submissions")
    public ResponseEntity<List<QuizSubmissionDto>> getSubmissionsByStudent(@PathVariable Long studentId) {
        List<QuizSubmission> submissions = quizSubmissionService.findByStudent(studentId);

        List<QuizSubmissionDto> result = submissions.stream()
                .map(quizMapper::toQuizSubmissionDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Подсчёт количества правильных ответов.
     *
     * @param quiz    квиз с вопросами и вариантами
     * @param answers карта "id вопроса → id выбранного варианта"
     * @return количество правильных ответов
     */
    private int calculateScore(Quiz quiz, Map<Long, Long> answers) {
        if (quiz.getQuestions() == null || answers == null || answers.isEmpty()) {
            return 0;
        }

        int score = 0;

        for (Question question : quiz.getQuestions()) {
            Long selectedOptionId = answers.get(question.getId());
            if (selectedOptionId == null) {
                continue;
            }

            if (question.getOptions() == null) {
                continue;
            }

            for (AnswerOption option : question.getOptions()) {
                if (option.getId().equals(selectedOptionId) && option.isCorrect()) {
                    score++;
                    break;
                }
            }
        }

        return score;
    }
}
