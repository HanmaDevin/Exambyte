package de.propra.exambyte.controller;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.controller.organizer.TestsController;
import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import de.propra.exambyte.model.Question;
import de.propra.exambyte.service.RoleAssignmentService;
import de.propra.exambyte.service.TestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestsController.class)
@Import(SecurityConfig.class)
public class TestControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RoleAssignmentService roleAssignmentService;

    @MockBean
    private TestService testService;

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @WithMockUser
    @DisplayName("Get Request auf /organizer/tests - Access Denied für nicht autorisierte Role")
    void test7() throws Exception {
        mvc.perform(get("/organizer/tests/"))
                .andExpect(forwardedUrl("/forbidden-access"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"STUDENT", "CORRECTOR"})
    @WithMockUser
    @DisplayName("Post Request auf /organizer/tests - Access Denied für nicht autorisierte Role trotz valider csrf()")
    void test8() throws Exception {
        mvc.perform(post("/organizer/tests/").with(csrf()))
                .andExpect(forwardedUrl("/forbidden-access"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("'/organizer/tests' ist erreichbar und liefert 'tests' wieder")
    void test_tests_page() throws Exception {
        mvc.perform(get("/organizer/tests"))
                .andExpect(status().isOk())
                .andExpect(view().name("tests"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf '/organizer/tests/new' liefert das view test-form wieder")
    void test_create_test_page() throws Exception {
        mvc.perform(get("/organizer/tests/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("test-form"))
                .andExpect(model().attributeExists("testDto"))
                .andExpect(model().attribute("testDto", Matchers.any(TestDto.class)));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Post Request auf /organizer/tests/new gibt ein redirect auf sich selbst und beinhaltet einen createdTest als attribute")
    void test_create_test_post() throws Exception {
        de.propra.exambyte.model.Test mockTest = mock(de.propra.exambyte.model.Test.class);
        Mockito.when(testService.createTest(Mockito.any(TestDto.class))).thenReturn(mockTest);

        mvc.perform(post("/organizer/tests/new").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/organizer/tests/new"))
                .andExpect(flash().attributeExists("createdTest"))
                .andExpect(flash().attribute("createdTest", mockTest));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("Get Request auf /organizer/tests/{id}/questions gibt 'questions' view mit passenden attributen wieder ")
    void test2() throws Exception {
        Long mockId = 1L;
        List<Question> mockQuestions = List.of(mock(Question.class));
        de.propra.exambyte.model.Test mockTest = mock(de.propra.exambyte.model.Test.class);

        Mockito.when(testService.getAllQuestions(mockId)).thenReturn(mockQuestions);
        Mockito.when(testService.findTestById(mockId)).thenReturn(mockTest);

        mvc.perform(get("/organizer/tests/" + mockId + "/questions"))
                .andExpect(status().isOk())
                .andExpect(view().name("questions"))
                .andExpect(model().attributeExists("questions", "test"))
                .andExpect(model().attribute("questions", mockQuestions))
                .andExpect(model().attribute("test", mockTest));

    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("TestNotFoundException gibt 'error/test-not-found' als view mit error als attribut wieder")
    void test3() throws Exception {
        Mockito.when(testService.findTestById(Mockito.anyLong())).thenThrow(new TestNotFoundException("Test not found"));

        mvc.perform(get("/organizer/tests/1/questions"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/test-not-found"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Test not found"));

    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("WrongDateInputException gibt 'test-form' als view mit error und testDto als attribut wieder")
    void test4() throws Exception {
        Mockito.when(testService.createTest(Mockito.any(TestDto.class))).thenThrow(new WrongDateInputException("Falsches Datum"));

        mvc.perform(post("/organizer/tests/new").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("test-form"))
                .andExpect(model().attributeExists("error", "testDto"))
                .andExpect(model().attribute("error", "Falsches Datum"))
                .andExpect(model().attribute("testDto", Matchers.any(TestDto.class)));

    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    @DisplayName("EmptyInputException gibt 'test-form' als view mit error und testDto als attribut wieder")
    void test5() throws Exception {
        Mockito.when(testService.createTest(Mockito.any(TestDto.class))).thenThrow(new EmptyInputException("Bitte alle Felder ausfüllen"));

        mvc.perform(post("/organizer/tests/new").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("test-form"))
                .andExpect(model().attributeExists("error", "testDto"))
                .andExpect(model().attribute("error", "Bitte alle Felder ausfüllen"))
                .andExpect(model().attribute("testDto", Matchers.any(TestDto.class)));

    }



}