package de.propra.exambyte.repository;

import de.propra.exambyte.model.FreeTextAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeTextAnswerRepository extends JpaRepository<FreeTextAnswer, Long> {
}
