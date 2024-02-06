package io.endeavour.stocks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.endeavour.stocks.UnitTestBase;
import io.endeavour.stocks.entity.crud.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc
class CrudControllerTest extends UnitTestBase {
    private static final Logger LOGGER= LoggerFactory.getLogger(CrudControllerTest.class);

    /**
     * This objectMapper is used to convert a json into an object and vice versa
     */
    ObjectMapper objectMapper=JsonMapper.builder().addModule(new JavaTimeModule()).build();

    ThreadLocal<Person> personThreadLocal=new ThreadLocal<>();

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getPerson_NotFound() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/crud/getPerson/?personID=983");
        mockMvc.perform(requestBuilder).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void getPerson_Exists()throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/crud/getPerson/?personID=203");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String outputResponseAsString = mvcResult.getResponse().getContentAsString();
        Person outputPerson = objectMapper.readValue(outputResponseAsString, Person.class);
        LOGGER.info("Person object returned from the API is {}",outputPerson);

        assertEquals("Ranbir",outputPerson.getFirstName());
    }


    /**
     * This method gets the json file and read the data and returns a string
     * @param filePath
     * @return
     */
    static String getJson(String filePath){
        Resource resource = new ClassPathResource(filePath);
        try {
            return Files.readString(resource.getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nested
    class UpdateDeletePersonTest{
        @BeforeEach
        public void insertPerson()throws Exception{
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/crud/insertPerson")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getJson("test-data/create-person.json"));
            LOGGER.info("Json of the person being inserted is {}",getJson("test-data/create-person.json"));

            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            String responseAsString = mvcResult.getResponse().getContentAsString();
            Person insertedPerson = objectMapper.readValue(responseAsString, Person.class);
            LOGGER.info("Person of the person being inserted is {}",insertedPerson);
            assertTrue(insertedPerson.getPersonID()!=0);
//            assertEquals("Dubai".toUpperCase(),insertedPerson.getLastName().toUpperCase());

            personThreadLocal.set(insertedPerson);

        }

        @Test
        public void updatePerson() throws Exception {
            Person person = personThreadLocal.get();
            LOGGER.info("person object before the update is {}",person);
            person.setFirstName("Dubai");
            person.setLastName("Sinu");

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/crud/updatePerson/?personID=" + person.getPersonID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(person));

            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            String outputResponseString = mvcResult.getResponse().getContentAsString();
            Person updatedPerson = objectMapper.readValue(outputResponseString, Person.class);
            LOGGER.info("person object After the API call is {}",person);

            assertEquals(person.getPersonID(),updatedPerson.getPersonID());
            assertEquals("Dubai".toUpperCase(),updatedPerson.getFirstName().toUpperCase());

        }

        @Test
        public void deletePerson() throws Exception {
           Person person= personThreadLocal.get();
            LOGGER.info("person object to be deleted is {}",person);
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/crud/deletePerson/" + person.getPersonID());
            mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

}