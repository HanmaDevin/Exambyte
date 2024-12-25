package de.propra.exambyte.repository;

import de.propra.exambyte.model.FreeTextQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeTextQuestionRepository extends JpaRepository<FreeTextQuestion, Long> {
}
