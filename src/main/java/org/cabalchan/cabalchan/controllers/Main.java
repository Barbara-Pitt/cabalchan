package org.cabalchan.cabalchan.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.cabalchan.cabalchan.entities.Attachment;
import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.entities.Filter;
import org.cabalchan.cabalchan.entities.Flag;
import org.cabalchan.cabalchan.entities.Notification;
import org.cabalchan.cabalchan.repositories.AttachmentRepository;
import org.cabalchan.cabalchan.repositories.BanRepository;
import org.cabalchan.cabalchan.repositories.CategoryRepository;
import org.cabalchan.cabalchan.repositories.EntryRepository;
import org.cabalchan.cabalchan.repositories.FilterRepository;
import org.cabalchan.cabalchan.repositories.FlagRepository;
import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.cabalchan.cabalchan.services.NotificationService;
import org.cabalchan.cabalchan.utilities.CommentUtil;
import org.cabalchan.cabalchan.utilities.FileUtil;
import org.cabalchan.cabalchan.utilities.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Controller
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
    private BanRepository banRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/new")
    public String entry(@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid, Model model){

        //notifications
        model.addAttribute("notificationCount", notificationService.getCount(cabaluuid));

        List<Flag> flags = flagRepository.findAll();       
        model.addAttribute("flaglist", flags);

        List<Filter> filters = filterRepository.findAll();       
        model.addAttribute("filterlist", filters);

        //categories
        model.addAttribute("categories", categoryRepository.findAll());

        return "newentry";
    }

    @PostMapping("/new")
    public String makeEntry(@RequestParam("spoiler") Optional<String> spoiler
                            ,@RequestParam("flag") Optional<String> flag
                            ,@RequestParam("filter") Optional<BigInteger> filter
                            ,@RequestParam("parentid") Optional<BigInteger> parent
                            ,@RequestParam("comment") String comment
                            ,@RequestParam("attachment") Optional<MultipartFile> file
                            ,@CookieValue(name="cabaluuid", required = false) Optional<Cookie> cabaluuid
                            ,HttpServletResponse response){

        Integer banned = banRepository.activeBans(
            IPUtil.getClientIpAddressIfServletRequestExist(), LocalDateTime.now());
        if (banned > 0 ){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Banned from the website");
        }

        Entry entry = new Entry();
        entry.setComment(CommentUtil.process(comment));
        entry.setCreateDate(LocalDateTime.now());
        entry.setIpaddr(IPUtil.getClientIpAddressIfServletRequestExist());
        //notifications cookie
        if (cabaluuid.isPresent()){
            entry.setCabalUUID(cabaluuid.get().getValue());
        } else {
            String cabalid = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("cabaluuid", cabalid);
            cookie.setMaxAge(365 * 24 * 60 * 60); // 365 days
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            entry.setCabalUUID(cabalid);
        }
        //end notifications cookie
        if(parent.isPresent()){
            Entry p = entryRepository.getById(parent.get());

            //check to see if post being replied too is beyond archive date
            Boolean old = p.getCreateDate().isBefore(LocalDateTime.now().minusDays(30));
            if (old){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Replying to old/archived post");
            }

            entry.setParent(p);

            Notification n = new Notification();

            n.setCreateDate(LocalDateTime.now());
            n.setCabalUUID(p.getCabalUUID());
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

                    if (filter.isPresent()){
                        Optional<Filter> f = filterRepository.findById(filter.get());
                        if (f.isPresent()){
                            attached.setFilter(f.get());
                        }
                    }
                    
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
}
