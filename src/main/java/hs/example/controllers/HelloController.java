package hs.example.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@RequestMapping("/persons")
    public String persons(){
        return "Helo Hieu";
    }
	
	@RequestMapping("/")
    public String firstMethod(){
        return "Helo Hieu";
    }
}
