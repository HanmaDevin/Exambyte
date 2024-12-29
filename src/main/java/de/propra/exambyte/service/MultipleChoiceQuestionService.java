package de.propra.exambyte.service;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.*;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.repository.MultipleChoiceQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;


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

    public MultipleChoiceQuestion updateMultipleChoiceQuestion(Long id, MultipleChoiceQuestionDto dto, List<String> deletedAnswers) {
        MultipleChoiceQuestion question = findMultipleChoiceQuestionById(id);
        dto.parseAnswers();


        if (deletedAnswers != null) {
            for (String deleted : deletedAnswers) {
                question.getAnswers().remove(deleted); // Entferne direkt aus der Map
                dto.getAnswers().remove(deleted);
                System.out.println("Deleted from Entity: " + deleted);
            }
        }

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

        if (!multipleChoiceQuestionDto.getAnswerBooleans().contains("true")) {
            throw new NoAnswersMarkedCorrectException("Mindestens eine Antwort muss als korrekt markiert werden.");
        }

        if (multipleChoiceQuestionDto.getAnswerTexts().size() != multipleChoiceQuestionDto.getAnswers().size()) {
            throw new NoAnswersMarkedCorrectException("Antworten dürfen nicht mehrfach vorkommen");
        }

        if(multipleChoiceQuestionDto.getAnswers().size() < 2){
            throw new InsufficientAnswersException("Es müssen mindestens 2 Antworten vorhanden sein");
        }


    }

    public MultipleChoiceQuestion findMultipleChoiceQuestionById(Long id) {
        return multipleChoiceQuestionRepository.findById(id).orElseThrow(() -> new MultipleChoiceQuestionNotFoundException("MultipleChoiceQuestion not found"));
    }
}
