# Unit Testing



## Importance of unit testing

- Unit testing it self is a huge topic of equal importance of functional and non functional project features.
- No one can persuade a person against unit testing and actually every team needs to find it way on how to increase code quality


## Arguments against unit testing

- Application is small and there is no need to have unit testing

   > Well.. I doubt that the application will always remain small and no maintenance will be needed.

---

   

## Arguments against unit testing (cont.)

- Not possible in our code due to complexity

   > Complex projects are hard to maintain and it is also hard when adding new features to be sure that something else is not affected. If one weights the risk and the complexity then it is obvious that without unit testing:
   >
   > - New bugs will keep appearing due to new features. 
   >
   > - Code quality improvements and design improvements are not possible because developers will be afraid of affecting the existing code.
   >
   > - Method/code duplication will be a **must do**
---


## Arguments against unit testing (cont.)

- Not possible in our code due to dependencies

   > This is where libraries that mock everything else exist. 
   >
   > This is where Inversion of Control of Spring assists
   >
   > Also making clean/small code -classes and methods with separation of responsibility assists.

---

## Arguments against unit testing (cont.)
- Not possible in our code due to legacy code structure

   > Time to gradually improve code quality and test the changes using unit testing

---

## Arguments against unit testing (cont.)
- Not possible in our code due knowledge of the team

   > Change this mindset in the team. 
   >
   > Self educate, Self organize, Work in agile methodology and use agile methodology to improve the quality of work
   >
   > Introduce code review where unit tests are mandatory
   >
   > e.g. You may establish a workshop at the beginning of Sprint and/or one unit testing day where everyone creates unit tests

---

## Arguments against unit testing (cont.)


- Not possible in our code due to time limitations/constraints/features

   > You have time limitations/constraints exactly because you spend time in fixing bugs or trying to work your way in code. 

---

   ## Arguments against unit testing (cont.)
- Unit testing makes changes in our code which increase the risk of the appearance of new bugs

   > Yes, this is true. Unit tests really need changes in code sometimes. But these changes actually are towards improving the overall code quality.

---

## Libraries, Concepts and ideas on Unit testing

There is a set of different kind of test which may be called as unit tests:

1. Java/class method unit tests

2. Overall functionality tests

3. Integration with other systems testing

4. Rest API Test (and Documentation) 

   

Depending on this one can identify which things to mock/spy/replace and which things need to remain as is.

- **Mock/Spy libraries:** Mockito, PowerMock
- **Replace:** Spring IoC and Profiles (org.springframework.boot:spring-boot-starter-test')
- **Mock other services:** Wiremock, Spring Cloud Contract Testing
- **Test and document Rest Api:** Swagger, Rest Docks, MockMVC

---

## JUnit Introduction

| Annotation               | Description                                                  |
| ------------------------ | ------------------------------------------------------------ |
| @Test                    | This annotation marks test methods.                          |
| @Before/@After           | These annotations mark methods that would run before or after every unit test |
| @BeforeClass/@AfterClass | These annotations mark methods that would run before or after test  class starts. Methods would be static |
---

## Assert Methods

| Method                                               | Description                                                  |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| assertTrue(expression)/<br />assertFalse(expression) | Test fails if expression is false/true                       |
| assertEquals(expected,actual)                        | Fails if values are not equal (Be careful of what equals means in your objects) |
| assertNull(obj)/assertNotNull(obj)                   | fails if given object is not null/is nul                     |
| Assertions.assertThat                                | Entry point for assertion methods for different types. Each method in this class is a static factory for a * type-specific assertion object. <br />Method assertThat exposes a big set of assertion methods which follow the natural language and is easy to follow |

---

## Quick Mockito Introduction

**Mocked** objects are objects which can be set in our code as if these were objects of a specific class but their behavior can be specified programmatically in our test code.

The same applies with **Spied** objects. The difference is that in a spy object any method that has not been mocked will behave as the real method of an object.

---

## Quick Mockito Introduction

**Mockito.verify:**

Verify number of method calls

e.g.

```java
Mockito.verify(mock, times(1)).mockMethod(); 
Mockito.verify(mock, never()).mockMethod(); 
```

**Mockito.when:**
Mock behavior

```java
when(preferences.getOrDefault(DateTimeUtils.DAYS, "0")).thenReturn("5");
doReturn(page).when(mock).findAll(any(Predicate.class), any(Pageable.class));
doReturn(page).when(mock).findAll(myPredicate, myPageable);
when(mock.mockMethod(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
when(mock.mockMethod(anyString(), anyString()))
      .thenAnswer(AdditionalAnswers.returnsFirstArg());
when(mock.mockMethod(anyString(), anyString()))
      .thenAnswer(AdditionalAnswers.returnsArgAt(2));
```

---

## Quick Mockito Introduction



**@Mock:** Creates a mocked object that latter on can be used in the tests

e.g.

```java
@Mock
private Preferences preferences;
```



**@MockBean:** Creates a mocked object that is also a spring bean -which is auto injected in our application wherever this is autowired-

e.g.

```java
    @MockBean
    private PersonRepository personRepository;
```

**@Spy/@SpyBean:** Creates a spy object/bean

```java
    @Spy
    Preferences preferences;
```

---

## Spring boot unit testing annotations

```java
@SpringBootTest
```

This marks a test as Spring Boot test. Which means that the complete (auto)configuration of the application will be used here.


```java
@ActiveProfiles("test")
```
This marks the active profile of the test. This means i.e. that the application-test.yml will be used in our tests. 

This methodology is most commonly used to specify database (as H2), embedded JMS, etc 

application-test.yml should be located in test/resources folder

---

## Spring boot unit testing annotations

```java
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
```

This Annotation marks the spring context as dirty. This means that one unit test will not affect the other in terms of Spring boot context. Commonly used when spring boot application context is affected but spring boot is not capable of knowing this e.g. durable JMS subscription exists.

> Use this in all tests when you got the feeling that one test class affects the others.



## JUnit Test Context (creating the application to test)

Spring boot Web application with postgresql connectivity.

**Entity:**

```java
@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
}
```

---

**Data transfer objects:**

```java
public class Preferences extends HashMap<String,String> {
}
```

```java
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
```

---

**Repository:**

```java
public interface PersonRepository extends JpaRepository<Person,Long>, QuerydslPredicateExecutor<Person> {
}
```

---

**Utility Service:**

```java
@AllArgsConstructor
@Service
public class DateTimeUtils {
    public static final String DAYS="DAYS";
    public static final String WEEKS = "WEEKS";
    public static final String YEARS = "YEARS";
    public static final String MONTHS = "MONTHS";

    public Preferences preferences;

    public ZonedDateTime  addDays(ZonedDateTime dateTime){
        dateTime=dateTime.plus(Period.ofDays(Integer.parseInt(preferences.getOrDefault(DAYS,"0"))));
        dateTime=dateTime.plus(Period.ofWeeks(Integer.parseInt(preferences.getOrDefault(WEEKS,"0"))));
        dateTime=dateTime.plus(Period.ofMonths(Integer.parseInt(preferences.getOrDefault(MONTHS,"0"))));
        dateTime=dateTime.plus(Period.ofYears(Integer.parseInt(preferences.getOrDefault(YEARS,"0"))));
        return dateTime;
    }
}
```

---

**Person Service**

```java
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    ...
    @Override
    public Page<PersonDTO> findPersonsByFirstName(String firstName, Pageable pageable) {
        return personRepository.findAll(QPerson.person.firstName.eq(firstName), pageable).map(this::mapToDTO);
    }
    @Override
    public PersonDTO save(PersonDTO personDTO) {
        return mapToDTO(personRepository.save(mapToEntity(personDTO)));
    }

    private Person mapToEntity(PersonDTO person) {
        return Person.builder().firstName(person.getFirstName())
                .lastName(person.getLastName()).id(person.getId()).build();
    }
    private PersonDTO mapToDTO(Person person) {
        return PersonDTO.builder().firstName(person.getFirstName())
                .lastName(person.getLastName()).id(person.getId()).build();
    }
}
```

---

**Rest interface:**

```java
@RestController
@RequiredArgsConstructor
public class PersonResource {
    public final PersonService personService;

    @GetMapping()
    public ResponseEntity get( Pageable pageable){
        return ResponseEntity.ok(personService.findAllPersons(pageable));
    }

    @GetMapping("firstName")
    public ResponseEntity get(@RequestParam String firstName, Pageable pageable){
        return ResponseEntity.ok(personService.findPersonsByFirstName(firstName,pageable));
    }

    @PostMapping
    public ResponseEntity save(@RequestBody PersonDTO personDTO){
        return ResponseEntity.ok(personService.save(personDTO));
    }
}
```

---

**Test profile:**

```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
  datasource:
    initialization-mode: always
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;
    username: sa
    password: sa
    platform: h2
    driverClassName: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    database: h2
    hibernate:
      ddl-auto: update
      naming:
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl
    show-sql: false
```

---

## Creating unit tests

### Simple Test

```java
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DateTimeUtilsTest {
    @Spy
//    @Mock
    Preferences preferences;
    @Test
    public void addDays() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils(preferences);
        when(preferences.getOrDefault(DateTimeUtils.DAYS, "0")).thenReturn("5");
//        when(preferences.getOrDefault(eq(DateTimeUtils.WEEKS), eq("0"))).thenReturn("0");
//        when(preferences.getOrDefault(eq(DateTimeUtils.MONTHS), eq("0"))).thenReturn("0");
//        when(preferences.getOrDefault(eq(DateTimeUtils.YEARS), anyString())).thenReturn("0");

        ZonedDateTime aDate = ZonedDateTime.of(2011, 2, 2, 13, 45, 11, 0, ZoneId.of("Europe/Athens"));
        ZonedDateTime dateTime = dateTimeUtils.addDays(aDate);
        Assertions.assertThat(dateTime).isEqualTo(aDate.plus(5, ChronoUnit.DAYS));
    }
}
```

---

### Mocked database access (Repository)

```java
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PersonServiceImplTest {
    @Autowired
    PersonService personService;
    
    @MockBean
    private PersonRepository personRepository;
    
    private Person createPerson(Long id, String firstName, String lastName) {
        return Person.builder().firstName(firstName).lastName(lastName).id(id).build();
    }
    private PersonDTO createPersonDTO(Long id, String firstName, String lastName) {
        return PersonDTO.builder().firstName(firstName).lastName(lastName).id(id).build();
    }
   ....
}
```
---
### Mocked database access (Repository) -cont.-

```java
 @Test
    public void findPersonByFirstName() {
        //prepare persons
        List<Person> personList = new ArrayList<>();
        personList.add(createPerson(1L, "Alex", "Rongas"));
        personList.add(createPerson(2L, "Alex", "Do"));
        PageImpl<Person> personPage = new PageImpl<>(personList, Pageable.unpaged(), personList.size());
        //mock repository
        doReturn(personPage).when(personRepository).findAll(any(Predicate.class), any(Pageable.class));
        Page<PersonDTO> page = personService.findPersonsByFirstName("Alex", Pageable.unpaged());
        assertThat(page.getTotalElements()).isEqualTo(2L);
        assertThat(page.getTotalPages()).isEqualTo(1L);
        assertThat(page.getContent()).hasSize(2);

        List<PersonDTO> personDTOList = new ArrayList<>();
        personDTOList.add(createPersonDTO(1L, "Alex", "Rongas"));
        personDTOList.add(createPersonDTO(2L, "Alex", "Do"));
        assertThat(page.getContent()).containsExactly(personDTOList.toArray(new PersonDTO[0]));
    }
```

---

### Using H2 as database

```java
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
...
}
```

---
### Using H2 as database (cont.)

```java
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
```

---

### Using H2 as database (cont.)

```groovy
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
  datasource:
    initialization-mode: always
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;
    username: sa
    password: sa
    platform: h2
    driverClassName: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    database: h2
    hibernate:
      ddl-auto: update
      naming:
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl
    show-sql: false
```

---

### Rest Interface testing

```java

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

   ...
}
```

---

### Rest Interface testing (cont.)

```java

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {MockMvcAutoConfiguration.class})
@ActiveProfiles("test")
public class PersonResourceTest {
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

  
}
```
---

### Rest Interface testing (cont.)

```java

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(value = {MockMvcAutoConfiguration.class})
@ActiveProfiles("test")
public class PersonResourceTest {
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
```
---

### Exercise

Unit test the version of Aggregator created in lecture `Lecture 6 Spring Data JMS/exersize`



