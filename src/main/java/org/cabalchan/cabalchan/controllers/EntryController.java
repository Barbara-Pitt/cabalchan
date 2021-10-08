package org.cabalchan.cabalchan.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.entities.Filter;
import org.cabalchan.cabalchan.entities.Flag;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.FilterRepository;
import org.cabalchan.cabalchan.repositories.FlagRepository;
import org.cabalchan.cabalchan.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Controller
public class EntryController {
    
    @Autowired
    private FlagRepository flagRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FilterRepository filterRepository;

    @GetMapping("/entry")
    public String entry(Model model
    ,@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid
    ,@RequestParam("entryid") BigInteger entryId
    ,@RequestParam("page") Optional<Integer> page){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(cabaluuid));

        Optional<Entry> p = entryRepository.findById(entryId);
        if(!p.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry Not Found");
        }
        model.addAttribute("OP", p.get());
        //old post?
        Boolean old = p.get().getCreateDate().isBefore(LocalDateTime.now().minusDays(30));
        model.addAttribute("oldOP", old);

        if(page.isPresent() && (page.get() > 0)){
            model.addAttribute("previous",page.get()-1);
            model.addAttribute("next",page.get()+1);
            model.addAttribute("entries", entryRepository.entriesPage(p.get(), PageRequest.of(page.get(),25)));
        } else {
            List<Flag> flags = flagRepository.findAll();       
            model.addAttribute("flaglist", flags);

            List<Filter> filters = filterRepository.findAll();       
            model.addAttribute("filterlist", filters);

            model.addAttribute("next",1);
            model.addAttribute("entries", entryRepository.entriesPage(p.get(), PageRequest.of(0,25)));
        } 
        return "entry";
    }
}
