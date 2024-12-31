package de.propra.exambyte.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
public class WebTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("Startseite ist als STUDENT erreichbar")
    void test1() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/home"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Startseite ist als ORGANIZER erreichbar")
    void test2() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/home"));
    }

    @Test
    @WithMockUser(roles = "CORRECTOR")
    @DisplayName("Startseite ist als CORRECTOR erreichbar")
    void test3() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/home"));
    }


    //TODO: Remove when removing Dashboard in final product
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
