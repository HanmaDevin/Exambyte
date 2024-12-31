package de.propra.exambyte.controller;


import de.propra.exambyte.controller.organizer.TestsModifyController;
import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import de.propra.exambyte.service.TestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TestsModifyController.class)
public class TestModifyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TestService testService;

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /{id}/edit - mit valider Test Id")
    void test1() throws Exception {
        Long mockTestId = 1L;
        de.propra.exambyte.model.Test mockTest = new de.propra.exambyte.model.Test("SampleTest", null, null, null);
        Mockito.when(testService.findTestById(mockTestId)).thenReturn(mockTest);

        mvc.perform(get("/organizer/tests/" + mockTestId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("test-form-modify"))
                .andExpect(model().attributeExists("currentTest", "testId"))
                .andExpect(model().attribute("testId", mockTestId))
                .andExpect(model().attribute("currentTest", Matchers.hasProperty("title", Matchers.is("SampleTest"))));

    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /{id}/edit - mit NICHT valider Test Id")
    void test2() throws Exception {
        Long mockTestId = 1L;
        Mockito.when(testService.findTestById(mockTestId)).thenThrow(new TestNotFoundException("Test not Found"));

        mvc.perform(get("/organizer/tests/" + mockTestId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Test not Found"));
    }


    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /{id}/edit - mit  valider Test Id und Data")
    void test3() throws Exception {
        Long mockTestId = 1L;
        TestDto modifiedTest = new TestDto("UpdatedTest", null, null, null);
        de.propra.exambyte.model.Test updatedTest = new de.propra.exambyte.model.Test("UpdatedTest", null, null, null);

        Mockito.when(testService.updateTest(Mockito.eq(mockTestId), Mockito.any(TestDto.class))).thenReturn(updatedTest);

        mvc.perform(post("/organizer/tests/" + mockTestId + "/edit")
                        .with(csrf())
                        .flashAttr("modifiedTest", modifiedTest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests"))
                .andExpect(flash().attributeExists("updatedTest"))
                .andExpect(flash().attribute("updatedTest", updatedTest));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /{id}/edit - mit  NICHT valider Test Id")
    void test4() throws Exception {
        Long mockTestId = 1L;
        TestDto modifiedTest = new TestDto("UpdatedTest", null, null, null);

        Mockito.when(testService.updateTest(Mockito.eq(mockTestId), Mockito.any(TestDto.class))).thenThrow(new TestNotFoundException("Test not Found"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/edit")
                        .with(csrf())
                        .flashAttr("modifiedTest", modifiedTest))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Test not Found"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /{id}/edit - mit  NICHT valider Test Data (EmptyInput)")
    void test5() throws Exception {
        Long mockTestId = 1L;
        TestDto emptyDto = new TestDto("", null, null, null);

        Mockito.when(testService.updateTest(Mockito.eq(mockTestId), Mockito.any(TestDto.class)))
                .thenThrow(new EmptyInputException("Bitte alle Felder ausfüllen"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/edit")
                        .with(csrf())
                        .flashAttr("modifiedTest", emptyDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/edit"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Bitte alle Felder ausfüllen"));
    }
    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /{id}/edit - mit  NICHT valider Test Data (WrongDate)")
    void test6() throws Exception {
        Long mockTestId = 1L;
        TestDto emptyDto = new TestDto("WrongDto", null, null, null);

        Mockito.when(testService.updateTest(Mockito.eq(mockTestId), Mockito.any(TestDto.class)))
                .thenThrow(new WrongDateInputException("Bitte Datum richtig ausfüllen"));

        mvc.perform(post("/organizer/tests/" + mockTestId + "/edit")
                        .with(csrf())
                        .flashAttr("modifiedTest", emptyDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/" + mockTestId + "/edit"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Bitte Datum richtig ausfüllen"));
    }
}
