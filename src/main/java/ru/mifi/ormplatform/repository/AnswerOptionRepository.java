package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.AnswerOption;

import java.util.List;

/**
 * Репозиторий для вариантов ответов (answer_option).
 */
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {

    List<AnswerOption> findAllByQuestion_Id(Long questionId);
}
