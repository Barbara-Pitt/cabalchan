package org.cabalchan.cabalchan.security;

import java.util.Optional;

import org.cabalchan.cabalchan.entities.User;
import org.cabalchan.cabalchan.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Register {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
	public String register(){
		return "register";
	}

	@PostMapping("/register")
	public String registerAccount(@RequestParam(name = "username") String username
                                ,@RequestParam(name = "password") String password){


		//check if username is unique
		Optional<User> dup = userRepository.findUserByUsername(username);
			
		if (dup.isPresent()){
			return "registerdup";
		} else {
			User newUser = new User();

			newUser.setUsername(username);
		
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(password);
			newUser.setPassword(encodedPassword);	
			newUser.setStaff(false);
		
			userRepository.save(newUser);
			//return to login screen
			return "redirect:/auth";
		}
	}
}
