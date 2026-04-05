package com.biharigraphic.jilamart.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin("*")
    public class PolicyController {

        @GetMapping("/privacy-policy")
        public String privacyPolicy() {
            // This will look for src/main/resources/templates/privacy-policy.html (Thymeleaf)
            return "privacy-policy";
        }
    }
