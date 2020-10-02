package com.logesh;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@GetMapping(value = "health")
	public String getSuccessMsg() {
		return "Success";
	}
	
	@GetMapping(value = "greet")
	public String greetUser(@RequestParam("name") String name) {
		return "Hello ! "+name.toUpperCase() +" ! Thank you for your visit!!";
	}
	
}
