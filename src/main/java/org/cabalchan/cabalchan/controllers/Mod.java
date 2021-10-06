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

import org.cabalchan.cabalchan.entities.Appeal;
import org.cabalchan.cabalchan.entities.Attachment;
import org.cabalchan.cabalchan.entities.Ban;
import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.entities.News;
import org.cabalchan.cabalchan.entities.Notification;
import org.cabalchan.cabalchan.entities.Report;
import org.cabalchan.cabalchan.entities.User;
import org.cabalchan.cabalchan.repositories.AppealRepository;
import org.cabalchan.cabalchan.repositories.AttachmentRepository;
import org.cabalchan.cabalchan.repositories.BanRepository;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.NewsRepository;
import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.cabalchan.cabalchan.repositories.ReportRepository;
import org.cabalchan.cabalchan.repositories.UserRepository;
import org.cabalchan.cabalchan.utilities.CommentUtil;
import org.cabalchan.cabalchan.utilities.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/mod")
public class Mod {

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
    AppealRepository appealRepository;

    @Autowired
    NewsRepository newsRepository;

    @GetMapping("/home")
    public String modhome(Model model){
        var reports = reportRepository.reportsPage(PageRequest.of(0,30));
        Integer appealCount = appealRepository.activeAppealCount();
        model.addAttribute("entries", reports);
        model.addAttribute("appeals", appealCount);
        return "mod";
    }

    @GetMapping("/posthistory")
    public String history(Model model
                         ,@RequestParam("ipaddr") String ipaddr){
        var postHistory = entryRepository.postHistory(ipaddr, PageRequest.of(0,30));
        model.addAttribute("entries", postHistory);
        return "posthistory";
    }

    @PostMapping("/removenews")
    public String newsRemove(@RequestParam(name="announcementid") BigInteger announcementId){
        Optional<News> newsItem = newsRepository.findById(announcementId);
        if(newsItem.isPresent()){
            newsRepository.delete(newsItem.get());
        }
        return "redirect:/news";
    }

    @GetMapping("/news")
    public String news(){
        return "announcements";
    }

    @PostMapping("/news")
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

    @GetMapping("/appeals")
    public String modappeal(Model model){
        var appeals = appealRepository.latestActiveAppeals(PageRequest.of(0,30));
        model.addAttribute("appeals", appeals);
        return "modappeals";
    }

    @PostMapping("/appeals")
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

    @PostMapping("/action")
    public String modAction(Principal principal
                            ,@RequestParam("actiontype") String actionType
                            ,@RequestParam("reportids[]") Optional<List<BigInteger>> reportIds
                            ,@RequestParam("banlength") Optional<Integer> banLength
                            ,@RequestParam("banreason") Optional<String> banReason
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