package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.*;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.RoleAssignmentService;
import de.propra.exambyte.service.TestService;
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

@WebMvcTest(MultipleChoiceQuestionsCreationController.class)
@Import(SecurityConfig.class)
public class MultipleChoiceQuestionsCreationControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private RoleAssignmentService roleAssignmentService;

    @MockBean
    MultipleChoiceQuestionService multipleChoiceQuestionService;


    @MockBean
    TestService testService;

    // Tests same Test with different roles
    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @DisplayName("Get Request auf /organizer/tests/{id}/mc-question - Access Denied für nicht autorisierte Role")
    void test6(String role) throws Exception {
        mvc.perform(get("/organizer/tests/1/mc-question")
                        .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - Access Denied für nicht autorisierte Role trotz valider csrf() ")
    void test7(String role) throws Exception {
        mvc.perform(post("/organizer/tests/1/mc-question")
                .with(csrf())
                .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id}/mc-question - mit valider Test ID: gibt 200 wieder und zeig das Erstell-Form mc-question-form mit passende Attribute an")
    void test1() throws Exception {
        Long mockTestId = 1L;

        mvc.perform(get("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(status().isOk())
                .andExpect(view().name("mc-question-form"))
                .andExpect(model().attributeExists("testId", "multipleChoiceQuestionDto"))
                .andExpect(model().attribute("testId", mockTestId));

    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id}/mc-question - mit NICHT valider Test ID: " +
            "TestNotFoundException wurde ausgelöst und auf error/test-not-found geleiten")
    void test2() throws Exception {
        Long mockTestId = 999999L;

        Mockito.when(testService.findTestById(mockTestId)).thenThrow(new TestNotFoundException("Test not found"));

        mvc.perform(get("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Test not found"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - mit valider Test ID und Data: " +
            "redirect zurück auf /organizer/tests/{id}/mc-question mit der erstellten Frage in Flash attribut")
    void test3() throws Exception {
        long mockTestId = 1L;

        MultipleChoiceQuestionDto questionDto = new MultipleChoiceQuestionDto("Sample Question", 10, "Explain", Map.of("1", true, "2", false));
        MultipleChoiceQuestion createdQuestion = new MultipleChoiceQuestion("Sample Question", 10, "Explain", Map.of("1", true, "2", false));

        Mockito.when(multipleChoiceQuestionService.createMultipleChoiceQuestion(Mockito.any(MultipleChoiceQuestionDto.class)))
                .thenReturn(createdQuestion);

        mvc.perform(post("/organizer/tests/" + mockTestId + "/mc-question")
                        .with(csrf())
                        .flashAttr("multipleChoiceQuestionDto", questionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(flash().attributeExists("createdQuestion"))
                .andExpect(flash().attribute("createdQuestion", createdQuestion));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - Access Denied ohne  CSRF Token")
    void test8() throws Exception {
        MultipleChoiceQuestionDto questionDto = new MultipleChoiceQuestionDto("Sample Question", 10, "Explain", Map.of("1", true, "2", false));

        mvc.perform(post("/organizer/tests/1/mc-question")
                        .flashAttr("multipleChoiceQuestionDto", questionDto))
                .andExpect(forwardedUrl("/forbidden-access"));
    }


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - mit NICHT valider Test Data (LowerOrEqualZeroException):" +
            " LowerOrEqualZeroException wird geworfen und es wird auf /organizer/tests/{id}/mc-question redirected mit passenden Fehler und einem neuen leeren Dto")
    void test4() throws Exception {
        long mockTestId = 1L;

        MultipleChoiceQuestionDto questionDto = new MultipleChoiceQuestionDto();

        Mockito.when(multipleChoiceQuestionService.createMultipleChoiceQuestion(Mockito.any(MultipleChoiceQuestionDto.class)))
                .thenThrow(new LowerOrEqualZeroException("Max Score muss größer-gleich 0 sein"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/mc-question")
                        .with(csrf())
                        .flashAttr("multipleChoiceQuestionDto", questionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(flash().attributeExists("multipleChoiceQuestionDto", "error"))
                .andExpect(flash().attribute("error", "Max Score muss größer-gleich 0 sein"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - mit NICHT valider Test Data (DuplicateAnswerException):" +
            " DuplicateAnswerException wird geworfen und es wird auf /organizer/tests/{id}/mc-question redirected mit passenden Fehler und einem neuen leeren Dto")
    void test4_b() throws Exception {
        long mockTestId = 1L;

        MultipleChoiceQuestionDto questionDto = new MultipleChoiceQuestionDto();

        Mockito.when(multipleChoiceQuestionService.createMultipleChoiceQuestion(Mockito.any(MultipleChoiceQuestionDto.class)))
                .thenThrow(new DuplicateAnswerException("Antworten dürfen nicht mehrfach vorkommen"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/mc-question")
                        .with(csrf())
                        .flashAttr("multipleChoiceQuestionDto", questionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(flash().attributeExists("multipleChoiceQuestionDto", "error"))
                .andExpect(flash().attribute("error", "Antworten dürfen nicht mehrfach vorkommen"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - mit NICHT valider Test Data (EmptyInputException):" +
            " EmptyInputException wird geworfen und es wird auf /organizer/tests/{id}/mc-question redirected mit passenden Fehler und einem neuen leeren Dto")
    void test4_c() throws Exception {
        long mockTestId = 1L;

        MultipleChoiceQuestionDto questionDto = new MultipleChoiceQuestionDto();

        Mockito.when(multipleChoiceQuestionService.createMultipleChoiceQuestion(Mockito.any(MultipleChoiceQuestionDto.class)))
                .thenThrow(new EmptyInputException("Alle Felder müssen befüllt sein"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/mc-question")
                        .with(csrf())
                        .flashAttr("multipleChoiceQuestionDto", questionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(flash().attributeExists("multipleChoiceQuestionDto", "error"))
                .andExpect(flash().attribute("error", "Alle Felder müssen befüllt sein"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - mit NICHT valider Test Data (NoAnswersMarkedCorrectException):" +
            " NoAnswersMarkedCorrectException wird geworfen und es wird auf /organizer/tests/{id}/mc-question redirected mit passenden Fehler und einem neuen leeren Dto")
    void test4_d() throws Exception {
        long mockTestId = 1L;

        MultipleChoiceQuestionDto questionDto = new MultipleChoiceQuestionDto();

        Mockito.when(multipleChoiceQuestionService.createMultipleChoiceQuestion(Mockito.any(MultipleChoiceQuestionDto.class)))
                .thenThrow(new NoAnswersMarkedCorrectException("Mindestens eine Antwort muss als korrekt markiert werden."));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/mc-question")
                        .with(csrf())
                        .flashAttr("multipleChoiceQuestionDto", questionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(flash().attributeExists("multipleChoiceQuestionDto", "error"))
                .andExpect(flash().attribute("error", "Mindestens eine Antwort muss als korrekt markiert werden."));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/mc-question - mit NICHT valider Test Data (InsufficientAnswersException):" +
            " InsufficientAnswersException wird geworfen und es wird auf /organizer/tests/{id}/mc-question redirected mit passenden Fehler und einem neuen leeren Dto")
    void test4_e() throws Exception {
        long mockTestId = 1L;

        MultipleChoiceQuestionDto questionDto = new MultipleChoiceQuestionDto();

        Mockito.when(multipleChoiceQuestionService.createMultipleChoiceQuestion(Mockito.any(MultipleChoiceQuestionDto.class)))
                .thenThrow(new InsufficientAnswersException("Es müssen mindestens 2 Antworten vorhanden sein"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/mc-question")
                        .with(csrf())
                        .flashAttr("multipleChoiceQuestionDto", questionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/mc-question"))
                .andExpect(flash().attributeExists("multipleChoiceQuestionDto", "error"))
                .andExpect(flash().attribute("error", "Es müssen mindestens 2 Antworten vorhanden sein"));
    }


}
