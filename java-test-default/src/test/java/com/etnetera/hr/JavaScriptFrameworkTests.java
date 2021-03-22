package com.etnetera.hr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Class used for Spring Boot/MVC based tests.
 *
 * @author Etnetera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JavaScriptFrameworkTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private JavaScriptFrameworkRepository repository;

    private void prepareData() throws Exception {
        JavaScriptFramework react = new JavaScriptFramework("ReactJS");
        JavaScriptFramework vue = new JavaScriptFramework("Vue.js");

        repository.save(react);
        repository.save(vue);
    }

    //private void prepareDataDelete() {
    //    JavaScriptFramework react = new JavaScriptFramework("ReactJS");
//
    //    repository.save(react);
    //}

    private void prepareDataOptionalValues() throws Exception {
        JavaScriptFramework react = new JavaScriptFramework(
                "ReactJS", "2.0", LocalDate.now(), 5);
        JavaScriptFramework vue = new JavaScriptFramework(
                "Vue.js", "3.0.1", LocalDate.of(2019, 12, 24), 1);

        repository.save(react);
        repository.save(vue);
    }

    @Test
    public void frameworkTest() throws Exception {
        prepareData();

        mockMvc.perform(get("/frameworks/1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ReactJS")))
                .andExpect(jsonPath("$.version", is("1.0.SNAPSHOT")));
    }

    @Test
    public void frameworksTest() throws Exception {
        prepareData();

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ReactJS")))
                .andExpect(jsonPath("$[0].version", is("1.0.SNAPSHOT")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Vue.js")))
                .andExpect(jsonPath("$[0].version", is("1.0.SNAPSHOT")));
    }

    @Test
    public void frameworksTestOptionalFields() throws Exception {
        prepareDataOptionalValues();

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ReactJS")))
                .andExpect(jsonPath("$[0].version", is("2.0")))
                .andExpect(jsonPath("$[0].deprecationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
                .andExpect(jsonPath("$[0].hypeLevel", is(5)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Vue.js")))
                .andExpect(jsonPath("$[1].version", is("3.0.1")))
                .andExpect(jsonPath("$[1].deprecationDate", is("2019-12-24")))
                .andExpect(jsonPath("$[1].hypeLevel", is(1)));
    }

    @Test
    public void frameworksTestUpdate() throws Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("ReactJSS");
        framework.setVersion("1.0.FINAL");
        repository.save(framework);

        mockMvc.perform(put("/frameworks/1").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("ReactJSS")))
                .andExpect(jsonPath("$.version", is("1.0.FINAL")));
    }

    @Test
    public void removeFramework() throws Exception {
        prepareData();

        mockMvc.perform(delete("/frameworks/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get("/frameworks")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Vue.js")))
                .andExpect(jsonPath("$[0].version", is("1.0.SNAPSHOT")));
    }

    @Test
    public void frameworkTestSearch() throws Exception {
        prepareDataOptionalValues();

        mockMvc.perform(get("/frameworks/search/ReactJS")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ReactJS")))
                .andExpect(jsonPath("$[0].version", is("2.0")))
                .andExpect(jsonPath("$[0].deprecationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
                .andExpect(jsonPath("$[0].hypeLevel", is(5)));

        mockMvc.perform(get("/frameworks/search/js")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("ReactJS")))
                .andExpect(jsonPath("$[0].version", is("2.0")))
                .andExpect(jsonPath("$[0].deprecationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
                .andExpect(jsonPath("$[0].hypeLevel", is(5)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Vue.js")))
                .andExpect(jsonPath("$[1].version", is("3.0.1")))
                .andExpect(jsonPath("$[1].deprecationDate", is("2019-12-24")))
                .andExpect(jsonPath("$[1].hypeLevel", is(1)));
    }

    @Test
    public void addFramework() throws JsonProcessingException, DataIntegrityViolationException, Exception {
        JavaScriptFramework framework = new JavaScriptFramework("AngularJS", "3.0.1", LocalDate.of(2019, 12, 24), 1);

        mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isOk());

    }

    @Test
    public void addFrameworkInvalid() throws JsonProcessingException, DataIntegrityViolationException, Exception {
        JavaScriptFramework framework = new JavaScriptFramework();
        repository.save(framework);
        mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

        framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
        mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("name")))
                .andExpect(jsonPath("$.errors[0].message", is("Size")));

    }

}
