package de.propra.exambyte.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(WebController.class)
public class WebTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "STUDENT")  // Simuliert einen eingeloggten User
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
    @DisplayName("Unberechtigte Benutz er erhalten Redirect (302)")
    void test_home_page_unauthenticated_redirect() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}
