package org.cabalchan.cabalchan.controllers;

import java.util.Optional;

import javax.servlet.http.Cookie;
import org.cabalchan.cabalchan.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RulesController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/rules")
    public String rules(@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid, Model model){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(cabaluuid));

        return "rules";
    }
}
