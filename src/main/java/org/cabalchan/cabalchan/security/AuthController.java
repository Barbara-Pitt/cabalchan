package org.cabalchan.cabalchan.security;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("")
    public String auth(@RequestParam(name = "error", required = false) Optional<String> error
                      ,@RequestParam(name = "logout", required = false) Optional<String> logout
                        ,Model model){

        //check to see if user logged in already
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            if (error.isPresent()){
                model.addAttribute("flashmsg","(Incorrect Login) - ");
            } else {
                if (logout.isPresent()){
                    model.addAttribute("flashmsg","(Logged out) - ");
                }
            }
            return "login";
        } else {
            String userName = authentication.getName();
			var userAuthorities = authentication.getAuthorities();
			model.addAttribute("username",userName);
			model.addAttribute("authorities",userAuthorities);
            for (var role : userAuthorities){
				if (role.getAuthority().equals("STAFF")){
					model.addAttribute("staff", true);
				}
			}
            return "loggedin";
        }
    }
}
