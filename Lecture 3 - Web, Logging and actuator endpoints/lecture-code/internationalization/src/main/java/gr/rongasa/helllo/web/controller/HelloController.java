package gr.rongasa.helllo.web.controller;

import gr.rongasa.helllo.config.HelloConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final HelloConfig helloConfig;

    @Value("${hello.config.track}")
    private boolean track;

    private List<String> names = new ArrayList<>();

    @GetMapping({"/"})
    public String hello(Model model, @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        model.addAttribute("name", name);
        if (name.compareToIgnoreCase("Word") != 0) {
            names.add(name);
            model.addAttribute("names", names);
        }
        return "index";
    }
}
