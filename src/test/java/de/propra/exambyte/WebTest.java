package de.propra.exambyte;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
public class WebTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die Startseite ist unter '/' erreichbar")
    @WithMockUser()
    @Disabled //Disabled for now
    void test_home_page() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Die Login-Seite ist unter '/login' erreichbar")
    @Disabled //Disabled for now
    void test_login_page() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Die Registrierungsseite ist unter '/register' erreichbar")
    @WithMockUser()
    @Disabled //Disabled for now
    void test_register_page() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andReturn();
    }
}