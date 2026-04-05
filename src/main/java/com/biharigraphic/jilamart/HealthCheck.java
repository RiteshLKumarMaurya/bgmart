package com.biharigraphic.jilamart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
@CrossOrigin("*")
public class HealthCheck {


    @GetMapping("/health")
    private ResponseEntity<String> checkHealth(){
        return new ResponseEntity<>("health is OK!!! 1.2 ",HttpStatus.OK);
    }




    @GetMapping("/check-jwt")
    private ResponseEntity<String> checkJwtWork(){
        return new ResponseEntity<>("Checking success with Access token!!",HttpStatus.OK);
    }

}
