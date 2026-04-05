package com.biharigraphic.jilamart.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin("*")
public class HomeController {

    @GetMapping("/")
    private String defaultWelcome(){
        return "home";
    }

}
