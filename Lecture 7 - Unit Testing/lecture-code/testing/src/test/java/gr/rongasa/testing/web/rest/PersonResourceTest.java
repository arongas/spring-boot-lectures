package gr.rongasa.testing.web.rest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import gr.rongasa.testing.domain.PersonDTO;
import gr.rongasa.testing.service.PersonService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {MockMvcAutoConfiguration.class})
@ActiveProfiles("test")
public class PersonResourceTest {


    @MockBean
    PersonService personService;
    @Autowired
    private MockMvc mockMvc;

    private URI baseUri;

    @Before
    public void setUp() throws URISyntaxException {
        baseUri = new URI("/");
        List<PersonDTO> personDTOList = new ArrayList<>();
        personDTOList.add(PersonDTO.builder().firstName("Alex").lastName("Rongas").build());
        personDTOList.add(PersonDTO.builder().firstName("Joe").lastName("Do").build());
        personDTOList.add(PersonDTO.builder().firstName("Joe2").lastName("Do2").build());
        PageImpl<PersonDTO> page = new PageImpl<>(personDTOList);
        when(personService.findAllPersons(any(Pageable.class))).thenReturn(page);
        when(personService.save(any(PersonDTO.class))).thenAnswer((Answer<PersonDTO>) invocation -> (PersonDTO) invocation.getArguments()[0]);
    }

    @Test
    public void get() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].firstName", is("Alex")))
                .andExpect(jsonPath("$.content[0].lastName", is("Rongas")))
                .andExpect(jsonPath("$.content[1].firstName", is("Joe")))
                .andExpect(jsonPath("$.content[1].lastName", is("Do")))
                .andExpect(jsonPath("$.content[2].firstName", is("Joe2")))
                .andExpect(jsonPath("$.content[2].lastName", is("Do2")))
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        DocumentContext json = JsonPath.parse(jsonResponse);
        assertThatJson(json).field("content[0]").field("firstName").isEqualTo("Alex");
    }

    @Test
    public void save() throws Exception {
        JSONObject person = new JSONObject()
                .put("firstName", "Alex")
                .put("lastName", "Rongas");

        mockMvc.perform(MockMvcRequestBuilders.post(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(person.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("firstName", is("Alex")))
                .andExpect(jsonPath("lastName", is("Rongas")));

    }
}