package org.cabalchan.cabalchan.controllers;

import org.cabalchan.cabalchan.repositories.AppealRepository;
import org.cabalchan.cabalchan.repositories.CategoryRepository;
import org.cabalchan.cabalchan.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Mod {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    AppealRepository appealRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/mod/home")
    public String modhome(Model model){
        var reports = reportRepository.reportsPage(PageRequest.of(0,30));
        Integer appealCount = appealRepository.activeAppealCount();
        model.addAttribute("entries", reports);
        model.addAttribute("appeals", appealCount);
        model.addAttribute("categories", categoryRepository.findAll());
        return "mod";
    }
}