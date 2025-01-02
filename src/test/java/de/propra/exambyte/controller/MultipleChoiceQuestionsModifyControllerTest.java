package de.propra.exambyte.controller;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.controller.organizer.MultipleChoiceQuestionsModifyController;
import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.*;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.RoleAssignmentService;
import de.propra.exambyte.service.TestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MultipleChoiceQuestionsModifyController.class)
@Import(SecurityConfig.class)
public class MultipleChoiceQuestionsModifyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleAssignmentService roleAssignmentService;

    @MockBean
    private MultipleChoiceQuestionService multipleChoiceQuestionService;
    @MockBean
    private TestService testService;

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/MultipleChoiceQuestion/{id_question} - Access Denied für nicht autorisierte Role")
    void test1(String role) throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId)
                        .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @DisplayName("Post Request auf /organizer/tests/{id_test}/questions/MultipleChoiceQuestion/{id_question} - Access Denied für nicht autorisierte Role trotz valider csrf()")
    void test2(String role) throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        mvc.perform(post("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId)
                        .with(csrf())
                        .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/MultipleChoiceQuestion/{id_question} mit valider TestId und questionId: gibt 200 wieder und multiple-choice-questions-modify.html mit passenden attributen")
    void test3() throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        MultipleChoiceQuestion mockQuestion = new MultipleChoiceQuestion("mockQuestion", 1, "mockExplanation", Map.of("1", true, "2", false));
        Mockito.when(multipleChoiceQuestionService.findMultipleChoiceQuestionById(mockQuestionId)).thenReturn(mockQuestion);


        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("question", "id_test", "id_question"))
                .andExpect(model().attribute("question", Matchers.hasProperty("questionText", Matchers.equalTo("mockQuestion"))))
                .andExpect(model().attribute("question", Matchers.hasProperty("maxScore", Matchers.equalTo(1))))
                .andExpect(model().attribute("question", Matchers.hasProperty("explanation", Matchers.equalTo("mockExplanation"))))
                .andExpect(model().attribute("id_test", mockTestId))
                .andExpect(model().attribute("id_question", mockQuestionId))
                .andExpect(view().name("organizer/editQuestions/multiple-choice-questions-modify"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/MultipleChoiceQuestion/{id_question} mit nicht valider TestId: löst TestNotFoundException aus und wird richtig gehandelt")
    void test4() throws Exception {
        long mockTestId = 9999L;
        long mockQuestionId = 1L;

        Mockito.when(testService.findTestById(mockTestId)).thenThrow(new TestNotFoundException("Test not found"));

        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Test not found"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/MultipleChoiceQuestion/{id_question} mit nicht valider questionId: löst MultipleChoiceQuestionNotFoundException aus und wird richtig gehandelt")
    void test4_b() throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 9999L;

        Mockito.when(multipleChoiceQuestionService.findMultipleChoiceQuestionById(mockQuestionId)).thenThrow(new MultipleChoiceQuestionNotFoundException("Multiple Choice Question not found"));

        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId))
                .andExpect(status().isOk())
                .andExpect(view().name("error/multiple-choice-question-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Multiple Choice Question not found"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id_test}/questions/MultipleChoiceQuestion/{id_question} mit validen Daten: speichert modifizierte Frage richtig und gibt 302 redirect wieder")
    void test5() throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        MultipleChoiceQuestionDto mockDto = new MultipleChoiceQuestionDto("Modified Question", 10, "Updated Explanation", Map.of("1", true, "2", false));
        MultipleChoiceQuestion updatedQuestion = new MultipleChoiceQuestion("Modified Question", 10, "Updated Explanation", Map.of("1", true, "2", false));


        Mockito.when(multipleChoiceQuestionService.updateMultipleChoiceQuestion(Mockito.eq(mockQuestionId), Mockito.any(MultipleChoiceQuestionDto.class), Mockito.any()))
                .thenReturn(updatedQuestion);

        mvc.perform(post("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId)
                        .with(csrf())
                        .flashAttr("modifiedQuestion", mockDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/questions"))
                .andExpect(flash().attributeExists("updatedQuestion"))
                .andExpect(flash().attribute("updatedQuestion", Matchers.hasProperty("questionText", Matchers.equalTo("Modified Question"))))
                .andExpect(flash().attribute("updatedQuestion", Matchers.hasProperty("maxScore", Matchers.equalTo(10))))
                .andExpect(flash().attribute("updatedQuestion", Matchers.hasProperty("explanation", Matchers.equalTo("Updated Explanation"))))
                .andExpect(flash().attribute("updatedQuestion", Matchers.hasProperty("answers", Matchers.equalTo(Map.of("1", true, "2", false)))));
    }

    @ParameterizedTest
    @ValueSource(classes = {LowerOrEqualZeroException.class, DuplicateAnswerException.class, EmptyInputException.class, NoAnswersMarkedCorrectException.class, InsufficientAnswersException.class})
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id_test}/questions/MultipleChoiceQuestion/{id_question} mit NICHT validen Daten: löst jeweilige Exception aus und wird richtig gehandelt")
    void test6(Class<? extends RuntimeException> currentException) throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        RuntimeException exceptionInstance = currentException.getConstructor(String.class).newInstance("Error: " + currentException.getSimpleName());

        Mockito.when(multipleChoiceQuestionService.updateMultipleChoiceQuestion(Mockito.eq(mockQuestionId), Mockito.any(MultipleChoiceQuestionDto.class), Mockito.any()))
                .thenThrow(exceptionInstance);

        mvc.perform(post("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/questions/MultipleChoiceQuestion/" + mockQuestionId))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Error: " + currentException.getSimpleName()));

    }

}
