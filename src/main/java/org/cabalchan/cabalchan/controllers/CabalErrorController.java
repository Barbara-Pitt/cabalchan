package org.cabalchan.cabalchan.controllers;

import java.time.LocalDateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.cabalchan.cabalchan.repositories.BanRepository;
import org.cabalchan.cabalchan.utilities.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CabalErrorController implements ErrorController  {

    @Autowired
    private BanRepository banRepository;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errormsg", "404 - Not Found");
                return "customerror";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errormsg", "500 - Internal Error");
                return "customerror";
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errormsg", "403 - Forbidden");
                model.addAttribute("banned"
                ,banRepository.activeBans(IPUtil.getClientIpAddressIfServletRequestExist()
                ,LocalDateTime.now()));
                return "customerror";
            }
        }
        return "error";
    }
}