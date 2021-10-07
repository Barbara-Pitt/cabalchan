package org.cabalchan.cabalchan.controllers;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("/")
    public String index(@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid, Model model
    ,@RequestParam("page") Optional<Integer> page){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(cabaluuid));

        if(page.isPresent() && (page.get() > 0)){
            model.addAttribute("previous",page.get()-1);
            model.addAttribute("next",page.get()+1);
            model.addAttribute("entries", entryRepository.threadsPage(PageRequest.of(page.get(),25)));
        } else {
            model.addAttribute("next",1);
            model.addAttribute("entries", entryRepository.threadsPage(PageRequest.of(0,25)));
        }
        return "index";
    }
}
