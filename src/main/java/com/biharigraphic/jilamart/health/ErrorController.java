package com.biharigraphic.jilamart.health;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/welcome-error")
    public String welcomeErrorHandle(){
        return "error/welcome_error";
    }

}
