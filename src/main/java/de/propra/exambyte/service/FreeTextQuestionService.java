package de.propra.exambyte.service;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.FreeTextQuestionNotFoundException;
import de.propra.exambyte.exception.LowerThanZeroException;
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
                freeTextQuestionDto.getQuestion(),
                freeTextQuestionDto.getPossibleAnswer(),
                freeTextQuestionDto.getMaxScore()

        );

        return freeTextQuestionRepository.save(freeTextQuestion);
    }

    private void validateFreeTextQuestion(FreeTextQuestionDto freeTextQuestionDto) {
        if (freeTextQuestionDto.getQuestion() == null || freeTextQuestionDto.getQuestion().isEmpty()) {
            throw new EmptyInputException("Question must not be empty");
        }

        if (freeTextQuestionDto.getPossibleAnswer() == null || freeTextQuestionDto.getPossibleAnswer().isEmpty()) {
            throw new EmptyInputException("Possible answer must not be empty");
        }

        if (freeTextQuestionDto.getMaxScore() <= 0) {
            throw new LowerThanZeroException("Max score must be greater than 0");
        }

    }

    public FreeTextQuestion findFreeTextQuestionById(Long id) {
        return freeTextQuestionRepository.findById(id).orElseThrow(() -> new FreeTextQuestionNotFoundException("FreeTextQuestion not found"));
    }

    public Long getId(Long id) {
        return freeTextQuestionRepository.getFreeTextQuestionById(id).getId();
    }
}
