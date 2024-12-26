package de.propra.exambyte;

import de.propra.exambyte.controller.organizer.TestsController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestsController.class)
public class TestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("The test page is accessible under '/organizer/tests' for users with the 'Organizer' role")
    @Disabled
    void test_tests_page() throws Exception {
        mvc.perform(get("/organizer/tests"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("The test creation page is accessible under '/organizer/tests/new'")
    @Disabled
    void test_create_test_page() throws Exception {
        mvc.perform(get("/organizer/tests/new"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("The post routing for the test creation page is accessible under '/organizer/tests/new'")
    @Disabled
    void test_create_test_post() throws Exception {
        mvc.perform(post("/organizer/tests/new"))
                .andExpect(status().isOk());
    }
}