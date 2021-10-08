package org.cabalchan.cabalchan.controllers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

import org.cabalchan.cabalchan.entities.Appeal;
import org.cabalchan.cabalchan.repositories.AppealRepository;
import org.cabalchan.cabalchan.repositories.AttachmentRepository;
import org.cabalchan.cabalchan.repositories.BanRepository;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ModAppealsController {
    @Autowired
    EntryRepository entryRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    BanRepository banRepository;

    @Autowired
    AppealRepository appealRepository;


    @GetMapping("/mod/appeals")
    public String modappeal(Model model){
        var appeals = appealRepository.latestActiveAppeals(PageRequest.of(0,30));
        model.addAttribute("appeals", appeals);
        return "modappeals";
    }

    @PostMapping("/mod/appeals")
    public String modappealdecision(@RequestParam("appealid") BigInteger appealId
                                    ,@RequestParam("status") Boolean status){
        Optional<Appeal> appeal = appealRepository.findById(appealId);
        if (appeal.isPresent()){
            if(status){
                var appealNew = appeal.get();
                appealNew.setAppealStatus(true);
                var appealBan = appealNew.getBan();
                appealBan.setExpirationDate(LocalDateTime.now());
                banRepository.save(appealBan);
                appealRepository.save(appealNew);
            } else {
                var appealNew = appeal.get();
                appealNew.setAppealStatus(false);
                appealRepository.save(appealNew);
            }
            
        }
        return "redirect:/mod/appeals";
    }
}
