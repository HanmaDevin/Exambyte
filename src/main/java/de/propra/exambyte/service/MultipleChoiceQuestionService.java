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


        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(
                multipleChoiceQuestionDto.getQuestionText(),
                multipleChoiceQuestionDto.getMaxScore(),
                multipleChoiceQuestionDto.getExplanation(),
                multipleChoiceQuestionDto.getAnswers()
                );
        System.out.println("Created Question: " + multipleChoiceQuestion);
        validateMultipleChoiceQuestion(multipleChoiceQuestion);
        return multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
    }

    public MultipleChoiceQuestion updateMultipleChoiceQuestion(Long id, MultipleChoiceQuestionDto dto, List<String> deletedAnswers) {

        MultipleChoiceQuestion question = findMultipleChoiceQuestionById(id);
        MultipleChoiceQuestion newQuestion = new MultipleChoiceQuestion(dto.getQuestionText(), dto.getMaxScore(), dto.getExplanation(), dto.getAnswers());
        validateMultipleChoiceQuestion(newQuestion);
        dto.parseAnswers();


        if (deletedAnswers != null) {
            for (String deleted : deletedAnswers) {
                question.getAnswers().remove(deleted); // Entferne direkt aus der Map
                dto.getAnswers().remove(deleted);
                System.out.println("Deleted from Entity: " + deleted);
            }
        }

        question.updateQuestion(newQuestion.getQuestionText(), newQuestion.getMaxScore(), newQuestion.getExplanation(), newQuestion.getAnswers());
        return multipleChoiceQuestionRepository.save(question);
    }

    public void validateMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        if (multipleChoiceQuestion.getQuestionText() == null || multipleChoiceQuestion.getQuestionText().isEmpty()) {
            throw new EmptyInputException("Frage darf nicht leer sein");
        }
        if (multipleChoiceQuestion.getAnswers() == null || multipleChoiceQuestion.getAnswers().isEmpty()) {
            throw new EmptyInputException("Antworten dürfen nicht leer sein");
        }

        if (multipleChoiceQuestion.getMaxScore() <= 0) {
            throw new LowerOrEqualZeroException("Punktanzahl muss größer als 0 sein");
        }

        if(multipleChoiceQuestion.getMaxScore() == null){
            throw new EmptyInputException("Punktanzahl darf nicht leer sein");
        }

        if (multipleChoiceQuestion.getExplanation() == null || multipleChoiceQuestion.getExplanation().isEmpty()) {
            throw new EmptyInputException("Erklärung darf nicht leer sein");
        }

        if (!multipleChoiceQuestion.getAnswers().containsValue(true)) {
            throw new NoAnswersMarkedCorrectException("Mindestens eine Antwort muss als korrekt markiert werden.");
        }

        if (multipleChoiceQuestion.getAnswers().size() != multipleChoiceQuestion.getAnswers().size()) {
            throw new DuplicateAnswerException("Antworten dürfen nicht mehrfach vorkommen");
        }

        if(multipleChoiceQuestion.getAnswers().size() < 2){
            throw new InsufficientAnswersException("Es müssen mindestens 2 Antworten vorhanden sein");
        }
    }

    public MultipleChoiceQuestion findMultipleChoiceQuestionById(Long id) {
        return multipleChoiceQuestionRepository.findById(id).orElseThrow(() -> new MultipleChoiceQuestionNotFoundException("MultipleChoiceQuestion not found"));
    }
}
