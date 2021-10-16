package org.cabalchan.cabalchan.controllers;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LatestController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("/latest")
    public String index(@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid
    ,Model model
    ,@RequestParam("page") Optional<Integer> page){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(cabaluuid));

        String next = "/latest/?page=";
        String prev = "/latest/?page=";

        Page<Entry> entries = null;
        if(page.isPresent() && page.get() > 0){
            next += page.get()+1;
            prev += page.get()-1;
            entries = entryRepository.latestPage(PageRequest.of(page.get(),25));
        } else {
            next += 1;
            entries = entryRepository.latestPage(PageRequest.of(0,25));
        }

        if (page.isPresent() && page.get() > 0){
            model.addAttribute("previous",prev);
        }
        model.addAttribute("next",next);
        model.addAttribute("entries", entries);
        return "index";
    }
}
