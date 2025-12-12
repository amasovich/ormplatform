package ru.mifi.ormplatform.service;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.*;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.enums.QuestionType;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class QuizSubmissionServiceIT {

    @Autowired private QuizSubmissionService quizSubmissionService;
    @Autowired private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private QuizRepository quizRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private AnswerOptionRepository answerOptionRepository;

    private User student;
    private Quiz quiz;
    private Question question;
    private AnswerOption correctOption;
    private AnswerOption wrongOption;

    @BeforeEach
    void setUp() {
        // ---------- User ----------
        student = new User();
        student.setName("Quiz Student");
        student.setEmail("quiz@student.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        // ---------- Category ----------
        Category category = new Category();
        category.setName("Testing");
        category = categoryRepository.save(category);

        // ---------- Course ----------
        Course course = new Course();
        course.setTitle("Quiz Course");
        course.setDescription("Course with quiz");
        course.setCategory(category);
        course.setTeacher(student); // не важно для теста
        course.setDuration(4);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);

        // ---------- Module ----------
        Module module = new Module();
        module.setCourse(course);
        module.setTitle("Module 1");
        module.setOrderIndex(1);
        module = moduleRepository.save(module);

        // ---------- Quiz ----------
        quiz = new Quiz();
        quiz.setModule(module);
        quiz.setTitle("Test Quiz");
        quiz.setTimeLimit(10);
        quiz = quizRepository.save(quiz);

        // ---------- Question ----------
        question = new Question();
        question.setQuiz(quiz);
        question.setText("2 + 2 = ?");
        question.setType(QuestionType.SINGLE_CHOICE);
        // ВАЖНО: добавляем в quiz.questions
        quiz.getQuestions().add(question);
        question = questionRepository.save(question);

        // ---------- Answer options ----------
        correctOption = new AnswerOption();
        correctOption.setQuestion(question);
        correctOption.setText("4");
        correctOption.setCorrect(true);
        // ВАЖНО: добавляем в question.options
        question.getOptions().add(correctOption);
        correctOption = answerOptionRepository.save(correctOption);

        wrongOption = new AnswerOption();
        wrongOption.setQuestion(question);
        wrongOption.setText("5");
        wrongOption.setCorrect(false);
        // ВАЖНО: добавляем в question.options
        question.getOptions().add(wrongOption);
        wrongOption = answerOptionRepository.save(wrongOption);
    }

    // ---------------------------------------------------------------------
    // SUCCESS
    // ---------------------------------------------------------------------

    @Test
    void student_can_pass_quiz_and_score_is_calculated() {
        Map<Long, Long> answers = new HashMap<>();
        answers.put(question.getId(), correctOption.getId());

        QuizSubmission submission =
                quizSubmissionService.evaluateAndSaveSubmission(
                        quiz.getId(),
                        student.getId(),
                        answers,
                        null
                );

        assertThat(submission).isNotNull();
        assertThat(submission.getScore()).isEqualTo(1);
        assertThat(submission.getQuiz().getId()).isEqualTo(quiz.getId());
        assertThat(submission.getStudent().getId()).isEqualTo(student.getId());
    }

    // ---------------------------------------------------------------------
    // REPEAT PROTECTION
    // ---------------------------------------------------------------------

    @Test
    void student_cannot_pass_quiz_twice() {
        Map<Long, Long> answers = Map.of(
                question.getId(), correctOption.getId()
        );

        quizSubmissionService.evaluateAndSaveSubmission(
                quiz.getId(),
                student.getId(),
                answers,
                null
        );

        assertThrows(ValidationException.class, () ->
                quizSubmissionService.evaluateAndSaveSubmission(
                        quiz.getId(),
                        student.getId(),
                        answers,
                        null
                )
        );
    }

    // ---------------------------------------------------------------------
    // FIND METHODS
    // ---------------------------------------------------------------------

    @Test
    void can_find_submissions_by_quiz() {
        quizSubmissionService.evaluateAndSaveSubmission(
                quiz.getId(),
                student.getId(),
                Map.of(question.getId(), correctOption.getId()),
                null
        );

        List<QuizSubmission> submissions =
                quizSubmissionService.findByQuiz(quiz.getId());

        assertThat(submissions).hasSize(1);
    }

    @Test
    void can_find_submissions_by_student() {
        quizSubmissionService.evaluateAndSaveSubmission(
                quiz.getId(),
                student.getId(),
                Map.of(question.getId(), correctOption.getId()),
                null
        );

        List<QuizSubmission> submissions =
                quizSubmissionService.findByStudent(student.getId());

        assertThat(submissions).hasSize(1);
    }
}
