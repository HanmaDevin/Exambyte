package de.propra.exambyte.service;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.LowerThanZeroException;
import de.propra.exambyte.model.FreeTextQuestion;
import org.springframework.stereotype.Service;

@Service
public class FreeTextQuestionService {

    public FreeTextQuestion createFreeTextQuestion(Long ID, FreeTextQuestionDto freeTextQuestionDto) {
        validateFreeTextQuestion(freeTextQuestionDto);

        return new FreeTextQuestion(ID,
                freeTextQuestionDto.getQuestion(),
                freeTextQuestionDto.getPossibleAnswer(),
                freeTextQuestionDto.getMaxScore());
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
}
