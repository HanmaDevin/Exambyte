package de.propra.exambyte.repository;

import de.propra.exambyte.model.FreeTextQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeTextQuestionRepository extends JpaRepository<FreeTextQuestion, Long> {
    FreeTextQuestion getFreeTextQuestionById(Long id);
}
