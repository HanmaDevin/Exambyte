package de.propra.exambyte.handler;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.controller.WebController;
import de.propra.exambyte.service.RoleAssignmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebController.class)
@Import({SecurityConfig .class, GlobalExceptionHandler.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleAssignmentService roleAssignmentService;

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR", "ORGANIZER"})
    @DisplayName("Access Denied Exception werden richtig gehandelt")
    void test1(String role) throws Exception {
        mvc.perform(get("/dummyError")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("error/forbidden-access"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Access Denied"));
    }
}
