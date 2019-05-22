package gr.rongasa.inventory.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GreetingsController {
    @GetMapping("/")
    public ResponseEntity<String> greetings(){
        return ResponseEntity.ok("Hello Spring boot!");
    }
}
