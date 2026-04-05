package com.biharigraphic.jilamart.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health-check")
    private String healthCHeck(){
        return "OKay, my honey!";
    }

}
