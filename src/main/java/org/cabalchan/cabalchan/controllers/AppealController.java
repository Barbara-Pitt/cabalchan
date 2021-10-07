package org.cabalchan.cabalchan.controllers;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.cabalchan.cabalchan.entities.Appeal;
import org.cabalchan.cabalchan.entities.Ban;
import org.cabalchan.cabalchan.repositories.AppealRepository;
import org.cabalchan.cabalchan.repositories.BanRepository;
import org.cabalchan.cabalchan.utilities.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Controller
public class AppealController {

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private AppealRepository appealRepository;

    @GetMapping("/appeal")
    public String appeal(Model model){
        List<Ban> bans = banRepository.currentBans(IPUtil.getClientIpAddressIfServletRequestExist(), LocalDateTime.now());
        model.addAttribute("bans", bans);
        return "banappeals";
    }

    @PostMapping("/appeal")
    public String banAppeal(@RequestParam("justification") Optional<String> justification
                            ,@RequestParam("banid") BigInteger banId){
        Ban b = banRepository.getById(banId);
        Appeal a = new Appeal();
        a.setBan(b);
        if (justification.isPresent()){
            a.setComment(Jsoup.clean(justification.get(), Safelist.none()));
        }
        a.setCreateDate(LocalDateTime.now());
        appealRepository.save(a);
        return "redirect:/appeal";
    }
}
