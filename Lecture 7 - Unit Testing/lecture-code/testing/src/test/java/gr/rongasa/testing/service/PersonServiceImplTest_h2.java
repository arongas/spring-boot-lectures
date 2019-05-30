package gr.rongasa.testing.service;

import gr.rongasa.testing.domain.Person;
import gr.rongasa.testing.domain.PersonDTO;
import gr.rongasa.testing.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PersonServiceImplTest_h2 {

    @Autowired
    PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @Before
    public void initTests() {
       personRepository.save(createPerson(1L, "Alex", "Rongas"));
       personRepository.save(createPerson(2L, "Alex", "Do"));
    }

    private Person createPerson(Long id, String firstName, String lastName) {
        return Person.builder().firstName(firstName).lastName(lastName).id(id).build();
    }

    private PersonDTO createPersonDTO(Long id, String firstName, String lastName) {
        return PersonDTO.builder().firstName(firstName).lastName(lastName).id(id).build();
    }

    @Test
    public void findPersonByFirstName() {

        Page<PersonDTO> page = personService.findPersonsByFirstName("Alex", Pageable.unpaged());

        assertThat(page.getTotalElements()).isEqualTo(2L);
        assertThat(page.getTotalPages()).isEqualTo(1L);
        assertThat(page.getContent()).hasSize(2);

        List<PersonDTO> personDTOList = new ArrayList<>();
        personDTOList.add(createPersonDTO(1L, "Alex", "Rongas"));
        personDTOList.add(createPersonDTO(2L, "Alex", "Do"));
        assertThat(page.getContent()).containsExactly(personDTOList.toArray(new PersonDTO[0]));
    }
}