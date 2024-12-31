package de.propra.exambyte.controller;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.service.RoleAssignmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import(SecurityConfig.class)
public class WebTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleAssignmentService roleAssignmentService;

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR", "ORGANIZER"})
    @WithMockUser()
    @DisplayName("Startseite ist als STUDENT, CORRECTOR und ORGANIZER erreichbar")
    void test1() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/home"));
    }

    @Test
    @DisplayName("Unberechtigte Benutz er erhalten Redirect (302)")
    void test_home_page_unauthenticated_redirect() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/oauth2/authorization/github"));
    }

}
