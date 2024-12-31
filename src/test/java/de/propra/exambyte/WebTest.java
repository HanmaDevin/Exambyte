package de.propra.exambyte;

import de.propra.exambyte.controller.WebController;
import de.propra.exambyte.repository.FreeTextAnswerRepository;
import de.propra.exambyte.repository.FreeTextQuestionRepository;
import de.propra.exambyte.repository.MultipleChoiceQuestionRepository;
import de.propra.exambyte.repository.TestRepository;
import de.propra.exambyte.service.TestService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(WebController.class)
public class WebTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TestService testService;
    @MockBean
    private EntityManager entityManager;
    @MockBean
    private FreeTextQuestionRepository freeTextQuestionRepository;
    @MockBean
    private TestRepository testRepository;
    @MockBean
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    @MockBean
    private FreeTextAnswerRepository freeTextAnswerRepository;





    @Test
    @WithMockUser(roles = "USER")  // Simuliert einen eingeloggten User
    @DisplayName("Startseite ist erreichbar")
    void test_home_page_accessible() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())  // Erwartet HTTP 200 OK
                .andExpect(view().name("public/home"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Dashboard ist erreichbar für Organizer")
    void test_dashboard_accessible() throws Exception {
        mvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/organizer"));
    }

    @Test
    @DisplayName("Unberechtigte Benutzer erhalten Redirect (302)")
    void test_home_page_unauthenticated_redirect() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}
