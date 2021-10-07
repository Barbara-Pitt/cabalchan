package org.cabalchan.cabalchan.controllers;

import org.cabalchan.cabalchan.entities.Report;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Controller
public class ReportController {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/report")
    public String report(Model model, @RequestParam("entryid") BigInteger entryId){
        model.addAttribute("entryId", entryId);
        return "report";
    }

    @PostMapping("/report")
    public String reportPost(@RequestParam("entryid") BigInteger entryId){
        Report r = new Report();
        r.setEntry(entryRepository.getById(entryId));
        r.setCreateDate(LocalDateTime.now());
        reportRepository.save(r);
        return "reportthanks";
    }
}
