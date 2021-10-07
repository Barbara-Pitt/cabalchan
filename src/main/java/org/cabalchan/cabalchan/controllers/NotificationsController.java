package org.cabalchan.cabalchan.controllers;

import java.util.Optional;

import javax.servlet.http.Cookie;
import org.cabalchan.cabalchan.entities.Notification;
import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Controller
public class NotificationsController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    public String notifications(@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid, Model model){

        //delete old notifications
        notificationRepository.deleteAllInBatch(notificationRepository.oldNotifications(LocalDateTime.now().minusDays(7)));

        model.addAttribute("notificationCount", "viewing");

        if (cabaluuid.isPresent()){
            model.addAttribute("notifications", notificationRepository.latestNotifications(cabaluuid.get().getValue(),PageRequest.of(0,30)));
        }
        
        return "/notifications";
    }

    @GetMapping("notif")
    public String notif(@RequestParam("notifid") BigInteger notificationId
    ,@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid){
        Optional<Notification> n = notificationRepository.findById(notificationId);
        if (n.isPresent() && cabaluuid.isPresent()){
            Notification notif = n.get();
            //check to see user's current cookie id is the same as the cookie id for that notification
            if(notif.getCabalUUID().equals(cabaluuid.get().getValue())){
                notif.setSeen(true);
                notificationRepository.save(notif);
                return "redirect:/entry?entryid=" + notif.getEntry().getId();
            } else {
                //user trying to use notification not belonging to them
                return "redirect:/notifications";
            }  
        }
        return "redirect:/notifications";
    }
}
