package de.propra.exambyte.controller.organizer.modifier;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.controller.organizer.FreeTextQuestionsModifyController;
import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.exception.FreeTextQuestionNotFoundException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.service.FreeTextQuestionService;
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


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(FreeTextQuestionsModifyController.class)
@Import(SecurityConfig.class)
public class FreeTextQuestionModifyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleAssignmentService roleAssignmentService;

    @MockBean
    private TestService testService;

    @MockBean
    private FreeTextQuestionService freeTextQuestionService;



    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/FreeTextQuestion/{id_question} - Access Denied für nicht autorisierte Role")
    void test_accessDeniedForUnauthorizedRolesOnGetRequest(String role) throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockQuestionId)
                        .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @DisplayName("Post Request auf /organizer/tests/{id_test}/questions/FreeTextQuestion/{id_question} - Access Denied für nicht autorisierte Role trotz valider csrf()")
    void test_accessDeniedForUnauthorizedRolesOnPostRequest(String role) throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        mvc.perform(post("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockQuestionId)
                        .with(csrf())
                        .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("GET /organizer/tests/{testId}/questions/FreeTextQuestion/{questionId} " +
            "liefert FreeTextQuestion-Formular mit allen notwendigen Attributen")
    public void test_showCreateFreeTextQuestionForm() throws Exception {
        Long mockTestId = 1L;
        Long mockquestionId = 1L;
        when(testService.findTestById(mockTestId)).thenReturn(mock(de.propra.exambyte.model.Test.class));
        when(freeTextQuestionService.findFreeTextQuestionById(mockquestionId)).thenReturn(mock(de.propra.exambyte.model.FreeTextQuestion.class));


        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockquestionId))
                .andExpect(status().isOk())
                .andExpect(view().name("organizer/editQuestions/free-text-questions-modify"))
                .andExpect(model().attributeExists("question", "id_test", "id_question"))
                .andExpect(model().attribute("id_question", mockquestionId))
                .andExpect(model().attribute("id_test", mockTestId))
                .andExpect(model().attribute("question", Matchers.any(FreeTextQuestionDto.class)));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/FreeTextQuestion/{id_question} mit nicht valider TestId: löst TestNotFoundException aus und wird richtig gehandelt")
    void test_handleTestNotFoundExceptionOnGetRequest() throws Exception {
        long mockTestId = 9999L;
        long mockQuestionId = 1L;

        Mockito.when(testService.findTestById(mockTestId)).thenThrow(new TestNotFoundException("Test not found"));

        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockQuestionId))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Test not found"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/FreeTextQuestion/{id_question} mit nicht valider questionId: löst FreeTextQuestionNotFoundException aus und wird richtig gehandelt")
    void test_handleFreeTextQuestionNotFoundExceptionOnGetRequest() throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 9999L;

        Mockito.when(freeTextQuestionService.findFreeTextQuestionById(mockQuestionId)).thenThrow(new FreeTextQuestionNotFoundException("error/free-text-question-not-found"));

        mvc.perform(get("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockQuestionId))
                .andExpect(status().isOk())
                .andExpect(view().name("error/free-text-question-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "error/free-text-question-not-found"));
    }


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id_test}/questions/FreeTextQuestion/{id_question} mit validen Daten: speichert modifizierte Frage richtig und gibt 302 redirect wieder")
    void test_saveModifiedFreeTextQuestion() throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        FreeTextQuestionDto mockDto = new FreeTextQuestionDto(" Question", 1, "Explanation");
        FreeTextQuestion updatedQuestion = new FreeTextQuestion("Modified Question",10, "Updated Explanation" );


        Mockito.when(freeTextQuestionService.updateFreeTextQuestion(Mockito.eq(mockQuestionId), Mockito.any(FreeTextQuestionDto.class)))
                .thenReturn(updatedQuestion);

        mvc.perform(post("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockQuestionId)
                        .with(csrf())
                        .flashAttr("modifiedQuestion", mockDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/questions"))
                .andExpect(flash().attributeExists("updatedQuestion"))
                .andExpect(flash().attribute("updatedQuestion", Matchers.hasProperty("questionText", Matchers.equalTo("Modified Question"))))
                .andExpect(flash().attribute("updatedQuestion", Matchers.hasProperty("maxScore", Matchers.equalTo(10))))
                .andExpect(flash().attribute("updatedQuestion", Matchers.hasProperty("possibleAnswer", Matchers.equalTo("Updated Explanation"))));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id_test}/questions/FreeTextQuestion/{id_question} mit nicht valider max Score Eingabe: löst LowerOrEqualZeroException aus und wird richtig gehandelt")
    void test_updateFreeTextQuestionWithLowerOrEqualZeroException() throws Exception {
        long mockTestId = 1L;
        long mockQuestionId = 1L;

        Mockito.when(freeTextQuestionService.updateFreeTextQuestion(Mockito.eq(mockTestId), Mockito.any(FreeTextQuestionDto.class)))
                .thenThrow(new LowerOrEqualZeroException("Max Score muss größer-gleich 0 sein"));


        mvc.perform(post("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockQuestionId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/questions/FreeTextQuestion/" + mockQuestionId))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Max Score muss größer-gleich 0 sein"));
    }

}
