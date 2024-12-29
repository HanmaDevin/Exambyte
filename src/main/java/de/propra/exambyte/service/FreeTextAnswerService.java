package de.propra.exambyte.service;

import de.propra.exambyte.dto.FreeTextAnswerDto;
import de.propra.exambyte.exception.FreeTextAnswerNotFoundException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.model.FreeTextAnswer;
import de.propra.exambyte.repository.FreeTextAnswerRepository;
import org.springframework.stereotype.Service;

@Service
public class FreeTextAnswerService {
    private final FreeTextAnswerRepository freeTextAnswerRepository;

    public FreeTextAnswerService(FreeTextAnswerRepository freeTextAnswerRepository) {
        this.freeTextAnswerRepository = freeTextAnswerRepository;
    }

    public FreeTextAnswer createFreeTextAnswer(FreeTextAnswerDto freeTextAnswerDto) {
        validateFreeTextAnswer(freeTextAnswerDto);

        FreeTextAnswer freeTextAnswer = new FreeTextAnswer(freeTextAnswerDto.getAnswer(), freeTextAnswerDto.getScore());

        return freeTextAnswerRepository.save(freeTextAnswer);

    }

    public FreeTextAnswer findFreeTextAnswerById(Long id) {
        return freeTextAnswerRepository.findById(id).orElseThrow(() -> new FreeTextAnswerNotFoundException("Freitextantwort wurde nicht gefunden"));
    }

    private void validateFreeTextAnswer(FreeTextAnswerDto freeTextAnswerDto) {
        if (freeTextAnswerDto.getScore() < 0) {
            throw new LowerOrEqualZeroException("Punktzahl darf nicht negativ sein");
        }
    }
}
