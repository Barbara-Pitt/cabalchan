package org.cabalchan.cabalchan.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;

import org.cabalchan.cabalchan.entities.Attachment;
import org.cabalchan.cabalchan.entities.Ban;
import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.entities.Notification;
import org.cabalchan.cabalchan.entities.Report;
import org.cabalchan.cabalchan.repositories.AttachmentRepository;
import org.cabalchan.cabalchan.repositories.BanRepository;
import org.cabalchan.cabalchan.repositories.CategoryRepository;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.cabalchan.cabalchan.repositories.ReportRepository;
import org.cabalchan.cabalchan.repositories.UserRepository;
import org.cabalchan.cabalchan.utilities.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ModActionController {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    BanRepository banRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/mod/action")
    public String modAction(Principal principal
                            ,@RequestParam("actiontype") String actionType
                            ,@RequestParam("reportids[]") Optional<List<BigInteger>> reportIds
                            ,@RequestParam("banlength") Optional<Integer> banLength
                            ,@RequestParam("banreason") Optional<String> banReason
                            ,@RequestParam("category") Optional<BigInteger> category
                            ,@RequestParam("banlengthcustom") Optional<String> banLengthCustom){
        if (reportIds.isPresent()){
            List<Report> r = reportRepository.findAllById(reportIds.get());
            for (Report report : r){
                if(actionType.equals("dismiss")){
                    reportRepository.delete(report);
                } else if (actionType.equals("spoiler")){
                    Entry e = report.getEntry();
                    Attachment a = e.getAttachment();
                    if (a != null){
                        a.setSpoiler(true);
                        attachmentRepository.save(a);
                        reportRepository.delete(report);
                    } 
                } else if (actionType.equals("unspoiler")){
                    Entry e = report.getEntry();
                    Attachment a = e.getAttachment();
                    if (a != null){
                        a.setSpoiler(false);
                        attachmentRepository.save(a);
                        reportRepository.delete(report);
                    } 
                } else if (actionType.equals("recategorize")){
                    Entry e = report.getEntry();
                    if (category.isPresent()){
                        if(category.get().equals(BigInteger.valueOf(0))){
                            e.setCategory(null);
                        } else {
                            e.setCategory(categoryRepository.getById(category.get()));
                        }
                    }
                    entryRepository.save(e);
                    reportRepository.delete(report);
                } else if(actionType.equals("warning")){
                    Entry e = report.getEntry();
                    String message = "<span class=\"modcomment\">"
                    + "<i class=\"bi bi-exclamation-triangle-fill\"></i> user has been warned for this post</span><br>";
                    if (banReason.isPresent()){
                        message = "<span class=\"modcomment\">"
                        + "<i class=\"bi bi-exclamation-triangle-fill\"></i> Warning: "
                        + banReason.get()
                        +"</span><br>";
                    } 
                    e.setComment(message + e.getComment());
                    entryRepository.save(e);

                    if(e.getCreateDate().isAfter(LocalDateTime.now().minusDays(7))){
                        Notification n = new Notification();
                        n.setCreateDate(LocalDateTime.now());
                        n.setEntry(e);
                        n.setMessageType("warning");
                        n.setSeen(false);
                        n.setCabalUUID(e.getCabalUUID());
                        notificationRepository.save(n);
                    }
                    
                    reportRepository.delete(report);
                } else if (actionType.equals("delete")){
                    Entry e = report.getEntry();

                    List<Entry> replies = e.getReplies();

                    for(Entry rep : replies){
                        rep.setParent(null);
                        entryRepository.save(rep);
                    }

                    //delete associated notifications
                    List<Notification> notifications = notificationRepository.findByEntry(e);
                    notificationRepository.deleteAll(notifications);

                    //delete associated reports
                    List<Report> reports = reportRepository.findByEntry(e);
                    reportRepository.deleteAll(reports);
                    
                    //delete attachment
                    Attachment a = e.getAttachment();
                    if (a != null){
                        Path p = Paths.get("./public/"
                        + a.getFilename()
                        );

                        try {
                            Files.delete(p);
                            attachmentRepository.delete(a);
                        } catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }

                    //delete entry
                    entryRepository.delete(e);
                } else if (actionType.equals("ban")){
                    Entry e = report.getEntry();
                    Ban b = new Ban();
                    var mod = userRepository.findUserByUsername(principal.getName());
                    if (mod.isPresent()){
                        b.setStaff(mod.get());
                        if(banReason.isPresent()){
                            b.setReason(banReason.get());
                        } else {
                            b.setReason("Other");
                        }
                        b.setIpaddr(e.getIpaddr());
                        if(banLengthCustom.isPresent() && !banLengthCustom.get().isBlank()){
                            try {
                                TemporalAmount customBanLength = TimeUtil.parse(Jsoup.clean(banLengthCustom.get(),Safelist.none()));
                                b.setExpirationDate(LocalDateTime.now().plus(customBanLength));
                            } catch (DateTimeParseException ex){
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrectly formatted date for custom ban length");
                            }
                        } else {
                            if (banLength.isPresent()){
                                Integer bannedFor = banLength.get();
                                switch (bannedFor) {
                                    case 1:  b.setExpirationDate(LocalDateTime.now().plusMinutes(5));
                                             break;
                                    case 2:  b.setExpirationDate(LocalDateTime.now().plusHours(3));
                                             break;
                                    case 3:  b.setExpirationDate(LocalDateTime.now().plusDays(1));
                                             break;
                                    case 4:  b.setExpirationDate(LocalDateTime.now().plusDays(3));
                                             break;
                                    case 5:  b.setExpirationDate(LocalDateTime.now().plusDays(7));
                                             break;
                                    case 6:  b.setExpirationDate(LocalDateTime.now().plusDays(14));
                                             break;
                                    case 7:  b.setExpirationDate(LocalDateTime.now().plusDays(30));
                                             break;
                                    case 8:  b.setExpirationDate(LocalDateTime.now().plusYears(100));
                                             break;
                                    default: b.setExpirationDate(LocalDateTime.now().plusDays(3));
                                             break;
                                }
                            } else {
                                b.setExpirationDate(LocalDateTime.now().plusDays(3));
                            }
                        }
                        b.setCreateDate(LocalDateTime.now());
                        b.setBlurb(e.getComment());
                        banRepository.save(b);

                        //add message to entry
                        String message = "<span class=\"modcomment\">"
                        + "<i class=\"bi bi-exclamation-triangle-fill\"></i> user has been banned for this post</span><br>";
                        if (banReason.isPresent()){
                            message = "<span class=\"modcomment\">"
                            + "<i class=\"bi bi-exclamation-triangle-fill\"></i> User banned for: "
                            + banReason.get()
                            +"</span><br>";
                        } 
                        e.setComment(message + e.getComment());
                        entryRepository.save(e);

                        reportRepository.delete(report);
                    }
                } else if (actionType.equals("bananddelete")){
                    Entry e = report.getEntry();
                    Ban b = new Ban();
                    var mod = userRepository.findUserByUsername(principal.getName());
                    if (mod.isPresent()){
                        b.setStaff(mod.get());
                        if(banReason.isPresent()){
                            b.setReason(banReason.get());
                        } else {
                            b.setReason("Other");
                        }
                        b.setIpaddr(e.getIpaddr());
                        if(banLengthCustom.isPresent() && !banLengthCustom.get().isBlank()){
                            try {
                                TemporalAmount customBanLength = TimeUtil.parse(Jsoup.clean(banLengthCustom.get(),Safelist.none()));
                                b.setExpirationDate(LocalDateTime.now().plus(customBanLength));
                            } catch (DateTimeParseException ex){
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrectly formatted date for custom ban length");
                            }
                        } else {
                            if (banLength.isPresent()){
                                Integer bannedFor = banLength.get();
                                switch (bannedFor) {
                                    case 1:  b.setExpirationDate(LocalDateTime.now().plusMinutes(5));
                                             break;
                                    case 2:  b.setExpirationDate(LocalDateTime.now().plusHours(3));
                                             break;
                                    case 3:  b.setExpirationDate(LocalDateTime.now().plusDays(1));
                                             break;
                                    case 4:  b.setExpirationDate(LocalDateTime.now().plusDays(3));
                                             break;
                                    case 5:  b.setExpirationDate(LocalDateTime.now().plusDays(7));
                                             break;
                                    case 6:  b.setExpirationDate(LocalDateTime.now().plusDays(14));
                                             break;
                                    case 7:  b.setExpirationDate(LocalDateTime.now().plusDays(30));
                                             break;
                                    case 8:  b.setExpirationDate(LocalDateTime.now().plusYears(100));
                                             break;
                                    default: b.setExpirationDate(LocalDateTime.now().plusDays(3));
                                             break;
                                }
                            } else {
                                b.setExpirationDate(LocalDateTime.now().plusDays(3));
                            }
                        }
                        b.setCreateDate(LocalDateTime.now());
                        b.setBlurb(e.getComment());
                        banRepository.save(b);
                    }
                    List<Entry> replies = e.getReplies();

                    for(Entry rep : replies){
                        rep.setParent(null);
                        entryRepository.save(rep);
                    }

                    //delete associated notifications
                    List<Notification> notifications = notificationRepository.findByEntry(e);
                    notificationRepository.deleteAll(notifications);

                    //delete associated reports
                    List<Report> reports = reportRepository.findByEntry(e);
                    reportRepository.deleteAll(reports);
                    
                    //delete attachment
                    Attachment a = e.getAttachment();
                    if (a != null){
                        Path p = Paths.get("./public/"
                        + a.getFilename()
                        );

                        try {
                            Files.delete(p);
                            attachmentRepository.delete(a);
                        } catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }

                    //delete entry
                    entryRepository.delete(e);
                }
            }
        }
        return "redirect:/mod/home";
    }
}
