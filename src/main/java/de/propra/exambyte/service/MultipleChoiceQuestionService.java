package de.propra.exambyte.service;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.exception.MultipleChoiceQuestionNotFoundException;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.repository.MultipleChoiceQuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class MultipleChoiceQuestionService {
    private final MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    public MultipleChoiceQuestionService(MultipleChoiceQuestionRepository multipleChoiceQuestionRepository) {
        this.multipleChoiceQuestionRepository = multipleChoiceQuestionRepository;
    }

    public MultipleChoiceQuestion createMultipleChoiceQuestion(MultipleChoiceQuestionDto multipleChoiceQuestionDto) {
        validateMultipleChoiceQuestion(multipleChoiceQuestionDto);

        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(
                multipleChoiceQuestionDto.getQuestionText(),
                multipleChoiceQuestionDto.getAnswers(),
                multipleChoiceQuestionDto.getMaxScore(),
                multipleChoiceQuestionDto.getExplanation()
        );

        return multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
    }

    public MultipleChoiceQuestion updateMultipleChoiceQuestion(Long id, MultipleChoiceQuestionDto dto) {
        MultipleChoiceQuestion question = findMultipleChoiceQuestionById(id);

        dto.parseAnswers();

        question.updateQuestion(dto.getQuestionText(), dto.getAnswers(), dto.getMaxScore(), dto.getExplanation());

        return multipleChoiceQuestionRepository.save(question);
    }

    private void validateMultipleChoiceQuestion(MultipleChoiceQuestionDto multipleChoiceQuestionDto) {
        if (multipleChoiceQuestionDto.getQuestionText() == null || multipleChoiceQuestionDto.getQuestionText().isEmpty()) {
            throw new EmptyInputException("Frage darf nicht leer sein");
        }
        if (multipleChoiceQuestionDto.getAnswers() == null || multipleChoiceQuestionDto.getAnswers().isEmpty()) {
            throw new EmptyInputException("Antworten dürfen nicht leer sein");
        }

        if (multipleChoiceQuestionDto.getMaxScore() <= 0) {
            throw new LowerOrEqualZeroException("Punktanzahl muss größer als 0 sein");
        }

        if (multipleChoiceQuestionDto.getExplanation() == null || multipleChoiceQuestionDto.getExplanation().isEmpty()) {
            throw new EmptyInputException("Erklärung darf nicht leer sein");
        }

        // TODO: check if there is at least one correct answer

        // TODO: check if there is no duplicate answer
    }

    public MultipleChoiceQuestion findMultipleChoiceQuestionById(Long id) {
        return multipleChoiceQuestionRepository.findById(id).orElseThrow(() -> new MultipleChoiceQuestionNotFoundException("MultipleChoiceQuestion not found"));
    }
}
