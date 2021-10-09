package org.cabalchan.cabalchan.controllers;

import java.util.Optional;

import javax.servlet.http.Cookie;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.NewsRepository;
import org.cabalchan.cabalchan.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;

@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("/news")
    public String news(@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid, Model model){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(cabaluuid));

        Integer activeIP = entryRepository.activeAddressCount(LocalDateTime.now().minusDays(3));
        Integer activePPH = entryRepository.activePPH(LocalDateTime.now().minusHours(1));
        Integer activeDailyIP = entryRepository.activeAddressCount(LocalDateTime.now().minusDays(1));
        Integer activeMonthlyIP = entryRepository.activeAddressCount(LocalDateTime.now().minusDays(30));
        
        model.addAttribute("activeips", activeIP);
        model.addAttribute("activepph", activePPH);
        model.addAttribute("activedailyips", activeDailyIP);
        model.addAttribute("activemonthlyips", activeMonthlyIP);

        //shows last 5 news entries
        var news = newsRepository.newsPage(PageRequest.of(0,5));
        model.addAttribute("news", news);
        return "news";
    }
}
