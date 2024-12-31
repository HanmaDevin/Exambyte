package de.propra.exambyte.controller;

import de.propra.exambyte.controller.organizer.FreeTextQuestionsCreationController;
import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.service.FreeTextQuestionService;
import de.propra.exambyte.service.TestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FreeTextQuestionsCreationController.class)
public class FreeTextQuestionCreationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TestService testService;

    @MockBean
    private FreeTextQuestionService freeTextQuestionService;

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf '/organizer/tests/{id}/ft-question' liefert das view 'ft-question-form' wieder"
        + " und die Attribute 'testId' und 'freeTextQuestionDto' sind vorhanden")
    public void test_showCreateFreeTextQuestionForm() throws Exception {
        Long mockTestId = 1L;

        mvc.perform(get("/organizer/tests/1/ft-question"))
            .andExpect(status().isOk())
            .andExpect(view().name("ft-question-form"))
            .andExpect(model().attributeExists("testId", "freeTextQuestionDto"))
            .andExpect(model().attribute("testId", mockTestId))
            .andExpect(model().attribute("freeTextQuestionDto", Matchers.any(FreeTextQuestionDto.class)));
    }

//    @Test
//    @WithMockUser(roles = "ORGANIZER")
//    @DisplayName("Post Request auf '/organizer/tests/{id}/ft-question' gibt ein redirect auf sich selbst " +
//            "und beinhaltet einen createdQuestion als Attribut und ")
//    public void test_addFreeTextQuestion() throws Exception {
//        Long mockTestId = 1L;
//    }
}
