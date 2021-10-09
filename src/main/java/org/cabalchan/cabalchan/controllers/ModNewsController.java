package org.cabalchan.cabalchan.controllers;

import java.math.BigInteger;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.cabalchan.cabalchan.entities.News;
import org.cabalchan.cabalchan.entities.User;
import org.cabalchan.cabalchan.repositories.NewsRepository;
import org.cabalchan.cabalchan.repositories.UserRepository;
import org.cabalchan.cabalchan.utilities.CommentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModNewsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NewsRepository newsRepository;

    @PostMapping("/mod/removenews")
    public String newsRemove(@RequestParam(name="announcementid") BigInteger announcementId){
        Optional<News> newsItem = newsRepository.findById(announcementId);
        if(newsItem.isPresent()){
            newsRepository.delete(newsItem.get());
        }
        return "redirect:/news";
    }

    @GetMapping("/mod/news")
    public String news(){
        return "announcements";
    }

    @PostMapping("/mod/news")
    public String newsNew(Principal principal, @RequestParam(name="news") String news){
        
        Optional<User> mod = userRepository.findUserByUsername(principal.getName());
        if (mod.isPresent()){
            News n = new News();
            n.setBlurb(CommentUtil.process(news));
            n.setCreateDate(LocalDateTime.now());
            n.setStaff(mod.get());
            newsRepository.save(n);
        }
        return "redirect:/news";
    }
}
