package org.cabalchan.cabalchan.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.cabalchan.cabalchan.entities.Appeal;
import org.cabalchan.cabalchan.entities.Attachment;
import org.cabalchan.cabalchan.entities.Ban;
import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.entities.Flag;
import org.cabalchan.cabalchan.entities.Notification;
import org.cabalchan.cabalchan.entities.Report;
import org.cabalchan.cabalchan.repositories.AppealRepository;
import org.cabalchan.cabalchan.repositories.AttachmentRepository;
import org.cabalchan.cabalchan.repositories.BanRepository;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.FlagRepository;
import org.cabalchan.cabalchan.repositories.NewsRepository;
import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.cabalchan.cabalchan.repositories.ReportRepository;
import org.cabalchan.cabalchan.services.NotificationService;
import org.cabalchan.cabalchan.utilities.CommentUtil;
import org.cabalchan.cabalchan.utilities.FileUtil;
import org.cabalchan.cabalchan.utilities.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;



@Controller
@RequestMapping("/")
public class Main {

    @Autowired
    private FlagRepository flagRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private AppealRepository appealRepository;

    @Autowired
    private NewsRepository newsRepository;
    
    @GetMapping("")
    public String index(HttpServletRequest request, Model model
    ,@RequestParam("page") Optional<Integer> page){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(request));

        if(page.isPresent() && (page.get() > 0)){
            model.addAttribute("previous",page.get()-1);
            model.addAttribute("next",page.get()+1);
            model.addAttribute("entries", entryRepository.threadsPage(PageRequest.of(page.get(),25)));
        } else {
            model.addAttribute("next",1);
            model.addAttribute("entries", entryRepository.threadsPage(PageRequest.of(0,25)));
        }
        return "index";
    }

    @GetMapping("/entry")
    public String entry(Model model
    ,HttpServletRequest request 
    ,@RequestParam("entryid") BigInteger entryId
    ,@RequestParam("page") Optional<Integer> page){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(request));

        Optional<Entry> p = entryRepository.findById(entryId);
        if(!p.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry Not Found");
        }
        model.addAttribute("OP", p.get());
        model.addAttribute("youid", request.getSession(false));
        //old post?
        Boolean old = p.get().getCreateDate().isBefore(LocalDateTime.now().minusDays(7));
        model.addAttribute("oldOP", old);

        if(page.isPresent() && (page.get() > 0)){
            model.addAttribute("previous",page.get()-1);
            model.addAttribute("next",page.get()+1);
            model.addAttribute("entries", entryRepository.entriesPage(p.get(), PageRequest.of(page.get(),25)));
        } else {
            List<Flag> flags = flagRepository.findAll();       
            model.addAttribute("flaglist", flags);
            model.addAttribute("next",1);
            model.addAttribute("entries", entryRepository.entriesPage(p.get(), PageRequest.of(0,25)));
        } 
        return "entry";
    }
    
    @GetMapping("faq")
    public String faq(HttpServletRequest request, Model model){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(request));

        return "faq";
    }

    @GetMapping("rules")
    public String rules(HttpServletRequest request, Model model){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(request));

        return "rules";
    }

    @GetMapping("news")
    public String news(HttpServletRequest request, Model model){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(request));

        Integer activeIP = entryRepository.activeAddressCount(LocalDateTime.now().minusDays(3));
        Integer activePPH = entryRepository.activePPH(LocalDateTime.now().minusHours(1));

        model.addAttribute("activeips", activeIP);
        model.addAttribute("activepph", activePPH);

        //shows last 5 news entries
        var news = newsRepository.newsPage(PageRequest.of(0,5));
        model.addAttribute("news", news);
        return "news";
    }

    @GetMapping("appeal")
    public String appeal(Model model){
        List<Ban> bans = banRepository.currentBans(IPUtil.getClientIpAddressIfServletRequestExist(), LocalDateTime.now());
        model.addAttribute("bans", bans);
        return "banappeals";
    }

    @PostMapping("appeal")
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

    @GetMapping("notifications")
    public String notifications(HttpServletRequest request, Model model){

        //delete old notifications
        notificationRepository.deleteAllInBatch(notificationRepository.oldNotifications(LocalDateTime.now().minusDays(7)));

        var session = request.getSession(false);
        model.addAttribute("notificationCount", "viewing");

        if (session != null){
            var s = RequestContextHolder.currentRequestAttributes().getSessionId();
            model.addAttribute("notifications", notificationRepository.latestNotifications(s,PageRequest.of(0,30)));
        }
        
        return "notifications";
    }

    @GetMapping("notif")
    public String notif(@RequestParam("notifid") BigInteger notificationId){
        Optional<Notification> n = notificationRepository.findById(notificationId);
        if (n.isPresent()){
            Notification notif = n.get();
            //check to see user's current session id is the same as the session id for that notification
            if(RequestContextHolder.currentRequestAttributes().getSessionId().equals(notif.getSessionId())){
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

    @GetMapping("new")
    public String entry(HttpServletRequest request, Model model){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(request));

        List<Flag> flags = flagRepository.findAll();       
        model.addAttribute("flaglist", flags);
        return "newentry";
    }

    @PostMapping("new")
    public String makeEntry(@RequestParam("spoiler") Optional<String> spoiler
                            ,@RequestParam("flag") Optional<String> flag
                            ,@RequestParam("parentid") Optional<BigInteger> parent
                            ,@RequestParam("comment") String comment
                            ,@RequestParam("attachment") Optional<MultipartFile> file){

        Integer banned = banRepository.activeBans(
            IPUtil.getClientIpAddressIfServletRequestExist(), LocalDateTime.now());
        if (banned > 0 ){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Banned from the website");
        }

        Entry entry = new Entry();
        entry.setComment(CommentUtil.process(comment));
        entry.setCreateDate(LocalDateTime.now());
        entry.setIpaddr(IPUtil.getClientIpAddressIfServletRequestExist());
        entry.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
        if(parent.isPresent()){
            Entry p = entryRepository.getById(parent.get());

            //check to see if post being replied too is beyond archive date
            Boolean old = p.getCreateDate().isBefore(LocalDateTime.now().minusDays(7));
            if (old){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Replying to old/archived post");
            }

            entry.setParent(p);

            Notification n = new Notification();

            n.setCreateDate(LocalDateTime.now());
            n.setSessionId(p.getSessionId());
            n.setMessageType("reply");
            n.setSeen(false);
            n.setEntry(p);

            notificationRepository.save(n);
        }

        if (flag.isPresent()){
            Flag f = flagRepository.findByFilename(flag.get());
            entry.setFlag(f);
        }
        if(file.isPresent() && !file.get().isEmpty()){
            final List<String> contentTypes = Arrays.asList(
            "image/png"
            ,"image/jpeg"
            ,"image/gif"
            ,"application/pdf"
            ,"audio/mpeg"
            ,"video/mp4"
            ,"video/webm"
            ,"application/zip"
            ,"audio/wav"
            ,"audio/ogg"
            ,"audio/mpeg");
            String fileContentType = file.get().getContentType();
            if(contentTypes.contains(fileContentType)){
                //save file in uploads folder
                MultipartFile attachedFile = file.get();
                String fileExt = FileUtil.getFileExt(attachedFile.getOriginalFilename());
                String newFileName = UUID.randomUUID().toString();
                try {
                    byte[] bytes = attachedFile.getBytes();
                    entryRepository.save(entry);

                    Path path = Paths.get("./public/"
                                        + newFileName + "." + fileExt
                                        );
                    Files.write(path, bytes); 

                    Attachment attached = new Attachment();
                    attached.setEntry(entry);
                    attached.setFilename(newFileName + "." + fileExt);
                    attached.setFiletype(fileContentType);
                    
                    //set spoiler
                    if (spoiler.isPresent() 
                    && !attached.getFiletype().equals("application/pdf") 
                    && !attached.getFiletype().equals("application/zip")
                    && !attached.getFiletype().equals("audio/wav")
                    && !attached.getFiletype().equals("audio/ogg")
                    && !attached.getFiletype().equals("audio/mpeg")
                    ){
                        attached.setSpoiler(true);
                    } else {
                        attached.setSpoiler(false);
                    }

                    attachmentRepository.save(attached);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //wrong filetype
            }
        } else {
            //no attachment post
            entryRepository.save(entry);
        }

        if(parent.isPresent()){
            return "redirect:/entry?entryid="+parent.get();
        } else {
            return "redirect:/";
        }
        
    }

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
