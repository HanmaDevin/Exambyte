package de.propra.exambyte.controller;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.controller.organizer.FreeTextQuestionsCreationController;
import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.exception.EmptyInputException;
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

@WebMvcTest(FreeTextQuestionsCreationController.class)
@Import(SecurityConfig.class)
public class FreeTextQuestionsCreationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TestService testService;

    @MockBean
    private FreeTextQuestionService freeTextQuestionService;

    @MockBean
    private RoleAssignmentService roleAssignmentService;


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf '/organizer/tests/{id}/ft-question' liefert das view 'ft-question-form' wieder"
            + " und die Attribute 'testId' und 'freeTextQuestionDto' sind vorhanden")
    public void test_showCreateFreeTextQuestionForm() throws Exception {
        Long mockTestId = 1L;
        when(testService.findTestById(mockTestId)).thenReturn(mock(de.propra.exambyte.model.Test.class));

        mvc.perform(get("/organizer/tests/1/ft-question"))
                .andExpect(status().isOk())
                .andExpect(view().name("ft-question-form"))
                .andExpect(model().attributeExists("testId", "freeTextQuestionDto"))
                .andExpect(model().attribute("testId", mockTestId))
                .andExpect(model().attribute("freeTextQuestionDto", Matchers.any(FreeTextQuestionDto.class)));
    }


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("GET /organizer/tests/{id}/ft-question - TestNotFoundException wird korrekt behandelt")
    void test_getWithNonExistentTestId() throws Exception {
        long mockTestId = 999L;

        // Simuliere, dass findTestById einen Fehler zurückgibt
        Mockito.when(testService.findTestById(mockTestId)).thenThrow(new TestNotFoundException("Test not Found"));


        mvc.perform(get("/organizer/tests/" + mockTestId + "/ft-question"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))  // Erwartet die Fehlerseite
                .andExpect(model().attributeExists("error"))  // Überprüft, ob das Model die Fehlernachricht enthält
                .andExpect(model().attribute("error", "Test not Found"));  // Erwartet die spezifische Fehlermeldung
    }


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf '/organizer/tests/{id}/ft-question' leitet auf '/organizer/tests/{id}/ft-question' weiter")
    void test_addFreeTextQuestion() throws Exception {
        long mockTestId = 1L;

        FreeTextQuestionDto questionDto = new FreeTextQuestionDto("Sample Question", 3, "asnwer");
        FreeTextQuestion createdQuestion = new FreeTextQuestion("Sample Question", "3", 3);

        Mockito.when(freeTextQuestionService.createFreeTextQuestion(Mockito.any(FreeTextQuestionDto.class)))
                .thenReturn(createdQuestion);

        mvc.perform(post("/organizer/tests/" + mockTestId + "/ft-question")
                        .with(csrf())
                        .flashAttr("freeTextQuestionDto", questionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/ft-question"))
                .andExpect(flash().attributeExists("createdQuestion"))
                .andExpect(flash().attribute("createdQuestion", createdQuestion));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/ft-question - TestNotFoundException führt zu Fehlerseite mit Status 404")
    void Test_addFreeTextQuestionWithTestNotFoundException() throws Exception {
        long mockTestId = 999L;
        FreeTextQuestionDto questionDto = new FreeTextQuestionDto("Sample Question", 3, "answer");

        Mockito.when(freeTextQuestionService.createFreeTextQuestion(Mockito.any(FreeTextQuestionDto.class)))
                .thenThrow(new TestNotFoundException("Test wurde nicht gefunden"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/ft-question")
                        .with(csrf())
                        .flashAttr("freeTextQuestionDto", questionDto))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))  // Erwartet richtige View-Template
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Test wurde nicht gefunden"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/ft-question - EmptyInputException führt zu Rendering des Formulars mit Fehlernachricht")
    void test_addFreeTextQuestionWithEmptyInputException() throws Exception {
        long mockTestId = 1L;
        FreeTextQuestionDto questionDto = new FreeTextQuestionDto("", 3, "");  // Leere Eingabe simulieren

        Mockito.when(freeTextQuestionService.createFreeTextQuestion(Mockito.any(FreeTextQuestionDto.class)))
                .thenThrow(new EmptyInputException("Frage darf nicht leer sein"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/ft-question")
                        .with(csrf())
                        .flashAttr("freeTextQuestionDto", questionDto))
                .andExpect(status().isOk())
                .andExpect(view().name("ft-question-form"))
                .andExpect(model().attributeExists("error", "freeTextQuestionDto"))
                .andExpect(model().attribute("error", "Frage darf nicht leer sein"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/ft-question - mit NICHT valider Test Data (LowerOrEqualZeroException):" +
            " Exception führt zu Rendering des Formulars mit Status 200 und Fehlernachricht")
    void test_addFreeTextQuestionWithValidationError() throws Exception {
        long mockTestId = 1L;
        FreeTextQuestionDto questionDto = new FreeTextQuestionDto();

        // Simuliert das Werfen der Exception
        Mockito.when(freeTextQuestionService.createFreeTextQuestion(Mockito.any(FreeTextQuestionDto.class)))
                .thenThrow(new LowerOrEqualZeroException("Max Score muss größer-gleich 0 sein"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/ft-question")
                        .with(csrf())
                        .flashAttr("freeTextQuestionDto", questionDto))
                .andExpect(status().isOk())  // 200 OK, weil die Exception zu einer erneuten Anzeige des Formulars führt
                .andExpect(view().name("ft-question-form"))  // Erwartet richtige View-Template
                .andExpect(model().attributeExists("error", "freeTextQuestionDto"))  // Fehler und neues DTO werden erwartet
                .andExpect(model().attribute("error", "Max Score muss größer-gleich 0 sein"));  // Überprüft Fehlermeldung
    }


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/{id}/ft-question - Access Denied ohne  CSRF Token")
    void test_access() throws Exception {
        FreeTextQuestionDto questionDto = new FreeTextQuestionDto("Sample Question", 3, "answer");

        mvc.perform(post("/organizer/tests/1/ft-question")
                        .flashAttr("freeTextQuestionDto", questionDto))
                .andExpect(forwardedUrl("/forbidden-access"));  // prüft auf 403
    }


    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})

    @DisplayName("GET /organizer/tests/{id}/ft-question - Zugriff verweigert für STUDENT und CORRECTOR (403)")
    void test_accessDeniedForUnauthorizedRolesOnGet(String role) throws Exception {
        mvc.perform(get("/organizer/tests/1/ft-question")
                        .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @DisplayName("POST /organizer/tests/{id}/ft-question - Zugriff verweigert für STUDENT und CORRECTOR (403)")
    void test_accessDeniedForUnauthorizedRolesOnPost(String role) throws Exception {
        mvc.perform(post("/organizer/tests/1/ft-question")
                        .with(csrf())
                        .with(user("user").roles(role)))
                .andExpect(forwardedUrl("/forbidden-access"));
    }


}