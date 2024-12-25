package de.propra.exambyte.service;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.FreeTextQuestionNotFoundException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.repository.FreeTextQuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class FreeTextQuestionService {
    private final FreeTextQuestionRepository freeTextQuestionRepository;

    public FreeTextQuestionService(FreeTextQuestionRepository freeTextQuestionRepository) {
        this.freeTextQuestionRepository = freeTextQuestionRepository;
    }

    public FreeTextQuestion createFreeTextQuestion(FreeTextQuestionDto freeTextQuestionDto) {
        validateFreeTextQuestion(freeTextQuestionDto);

        FreeTextQuestion freeTextQuestion = new FreeTextQuestion(
                freeTextQuestionDto.getQuestionText(),
                freeTextQuestionDto.getPossibleAnswer(),
                freeTextQuestionDto.getMaxScore()

        );

        return freeTextQuestionRepository.save(freeTextQuestion);
    }

    private void validateFreeTextQuestion(FreeTextQuestionDto freeTextQuestionDto) {
        if (freeTextQuestionDto.getQuestionText() == null || freeTextQuestionDto.getQuestionText().isEmpty()) {
            throw new EmptyInputException("Die Fragestellung darf nicht leer sein");
        }

        if (freeTextQuestionDto.getPossibleAnswer() == null || freeTextQuestionDto.getPossibleAnswer().isEmpty()) {
            throw new EmptyInputException("Antworten dürfen nicht leer sein");
        }

        if (freeTextQuestionDto.getMaxScore() <= 0) {
            throw new LowerOrEqualZeroException("Punktzahl muss größer als 0 sein");
        }

    }

    public FreeTextQuestion findFreeTextQuestionById(Long id) {
        return freeTextQuestionRepository.findById(id).orElseThrow(() -> new FreeTextQuestionNotFoundException("Freitextaufgabe wurde nicht gefunden"));
    }

    public Long getId(Long id) {
        return freeTextQuestionRepository.getFreeTextQuestionById(id).getId();
    }
}
