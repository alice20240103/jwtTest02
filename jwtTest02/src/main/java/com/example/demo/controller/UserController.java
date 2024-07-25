package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	 @GetMapping("/profile")
	    public String showUserProfile() {	    
		 	System.out.println("showUserProfile 동작함");
	        return "userProfile";
	    }
}
