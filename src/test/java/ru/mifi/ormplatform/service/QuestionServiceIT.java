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
import ru.mifi.ormplatform.repository.AnswerOptionRepository;
import ru.mifi.ormplatform.repository.QuestionRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QuestionServiceIT {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    private Quiz quiz;

    @BeforeEach
    void setUp() {
        User teacher = userService.createUser(
                "Teacher",
                "teacher@test.com",
                UserRole.TEACHER
        );

        Category category = categoryService.createCategory("Test category");

        Course course = courseService.createCourse(
                "Test course",
                "Description",
                category.getId(),
                teacher.getId(),
                5,
                LocalDate.now()
        );

        Module module = moduleService.createModule(
                course.getId(),
                "Module",
                1,
                "Module desc"
        );

        quiz = quizService.createQuiz(
                course.getId(),
                module.getId(),
                "Quiz",
                10
        );
    }

    // ---------------------------------------------------------------------
    // CREATE QUESTION
    // ---------------------------------------------------------------------

    @Test
    void createQuestion_singleChoice_success() {
        Question question = questionService.createQuestion(
                quiz.getId(),
                "What is ORM?",
                QuestionType.SINGLE_CHOICE
        );

        assertNotNull(question.getId());
        assertEquals(QuestionType.SINGLE_CHOICE, question.getType());
        assertEquals("What is ORM?", question.getText());
    }

    @Test
    void createQuestion_emptyText_throwsException() {
        assertThrows(ValidationException.class, () ->
                questionService.createQuestion(
                        quiz.getId(),
                        "   ",
                        QuestionType.TEXT
                )
        );
    }

    // ---------------------------------------------------------------------
    // ANSWER OPTIONS
    // ---------------------------------------------------------------------

    @Test
    void singleChoice_allowsOnlyOneCorrectOption() {
        Question question = questionService.createQuestion(
                quiz.getId(),
                "Single choice",
                QuestionType.SINGLE_CHOICE
        );

        questionService.addAnswerOption(question.getId(), "Correct", true);

        assertThrows(ValidationException.class, () ->
                questionService.addAnswerOption(
                        question.getId(),
                        "Another correct",
                        true
                )
        );
    }

    @Test
    void multipleChoice_allowsMultipleCorrectOptions() {
        Question question = questionService.createQuestion(
                quiz.getId(),
                "Multiple choice",
                QuestionType.MULTIPLE_CHOICE
        );

        questionService.addAnswerOption(question.getId(), "A", true);
        questionService.addAnswerOption(question.getId(), "B", true);

        assertEquals(
                2,
                answerOptionRepository.findAllByQuestion_Id(question.getId())
                        .stream()
                        .filter(AnswerOption::isCorrect)
                        .count()
        );
    }

    @Test
    void textQuestion_cannotHaveOptions() {
        Question question = questionService.createQuestion(
                quiz.getId(),
                "Text question",
                QuestionType.TEXT
        );

        assertThrows(ValidationException.class, () ->
                questionService.addAnswerOption(
                        question.getId(),
                        "Invalid option",
                        false
                )
        );
    }

    // ---------------------------------------------------------------------
    // UPDATE QUESTION
    // ---------------------------------------------------------------------

    @Test
    void updateQuestion_changeText_success() {
        Question question = questionService.createQuestion(
                quiz.getId(),
                "Old text",
                QuestionType.TEXT
        );

        Question updated = questionService.updateQuestion(
                question.getId(),
                "New text",
                null
        );

        assertEquals("New text", updated.getText());
    }

    @Test
    void updateQuestion_cannotChangeToTextIfOptionsExist() {
        Question question = questionService.createQuestion(
                quiz.getId(),
                "Question",
                QuestionType.SINGLE_CHOICE
        );

        questionService.addAnswerOption(question.getId(), "Option", true);

        assertThrows(ValidationException.class, () ->
                questionService.updateQuestion(
                        question.getId(),
                        null,
                        QuestionType.TEXT
                )
        );
    }

    // ---------------------------------------------------------------------
    // DELETE QUESTION
    // ---------------------------------------------------------------------

    @Test
    void deleteQuestion_removesQuestionAndOptions() {
        Question question = questionService.createQuestion(
                quiz.getId(),
                "To delete",
                QuestionType.MULTIPLE_CHOICE
        );

        AnswerOption option = questionService.addAnswerOption(
                question.getId(),
                "Option",
                false
        );

        questionService.deleteQuestion(question.getId());

        assertFalse(questionRepository.existsById(question.getId()));
        assertFalse(answerOptionRepository.existsById(option.getId()));
    }
}
