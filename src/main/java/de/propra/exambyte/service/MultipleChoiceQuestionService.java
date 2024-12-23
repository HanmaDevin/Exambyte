package de.propra.exambyte.service;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
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
                multipleChoiceQuestionDto.getAnswersOptions(),
                multipleChoiceQuestionDto.getCorrectAnswers(),
                multipleChoiceQuestionDto.getMaxScore(),
                multipleChoiceQuestionDto.getExplanation()

        );

        return multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
    }

    //TODO: Add validation, throw custom Exception and write hanldermethod accordingly
    private void validateMultipleChoiceQuestion(MultipleChoiceQuestionDto multipleChoiceQuestionDto) {

    }

    public MultipleChoiceQuestion findMultipleChoiceQuestionById(Long id) {
        return multipleChoiceQuestionRepository.findById(id).orElseThrow(() -> new MultipleChoiceQuestionNotFoundException("MultipleChoiceQuestion not found"));
    }
}
