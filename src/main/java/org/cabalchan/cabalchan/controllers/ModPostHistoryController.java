package org.cabalchan.cabalchan.controllers;

import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModPostHistoryController {

    @Autowired
    EntryRepository entryRepository;

    @GetMapping("/mod/posthistory")
    public String history(Model model
                         ,@RequestParam("ipaddr") String ipaddr){
        var postHistory = entryRepository.postHistory(ipaddr, PageRequest.of(0,30));
        model.addAttribute("entries", postHistory);
        return "posthistory";
    }
}
