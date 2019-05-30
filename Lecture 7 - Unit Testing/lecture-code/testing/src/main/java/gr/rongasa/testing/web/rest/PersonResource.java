package gr.rongasa.testing.web.rest;

import gr.rongasa.testing.domain.PersonDTO;
import gr.rongasa.testing.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
