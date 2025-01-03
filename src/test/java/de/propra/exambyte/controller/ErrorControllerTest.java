package de.propra.exambyte.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ErrorController.class)
public class ErrorControllerTest {

    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @ValueSource(strings = {"ORGANIZER", "CORRECTOR", "STUDENT"})
    @DisplayName("/forbidden-access gibt das richtige Template zurück")
    void test1(String role) throws Exception {
        mvc.perform(get("/forbidden-access")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("error/forbidden-access"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ORGANIZER", "CORRECTOR", "STUDENT"})
    @DisplayName("/test-not-found gibt das richtige Template zurück")
    void test2(String role) throws Exception {
        mvc.perform(get("/test-not-found")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ORGANIZER", "CORRECTOR", "STUDENT"})
    @DisplayName("/free-text-question-not-found gibt das richtige Template zurück")
    void test3(String role) throws Exception {
        mvc.perform(get("/free-text-question-not-found")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("error/free-text-question-not-found"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ORGANIZER", "CORRECTOR", "STUDENT"})
    @DisplayName("/multiple-choice-question-not-found gibt das richtige Template zurück")
    void test4(String role) throws Exception {
        mvc.perform(get("/multiple-choice-question-not-found")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("error/multiple-choice-question-not-found"));
    }

}
