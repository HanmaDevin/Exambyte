package de.propra.exambyte.repository;

import de.propra.exambyte.model.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultipleChoiceQuestionRepository extends JpaRepository<MultipleChoiceQuestion, Long> {
}
