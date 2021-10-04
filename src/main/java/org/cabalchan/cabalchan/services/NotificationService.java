package org.cabalchan.cabalchan.services;

import javax.servlet.http.HttpServletRequest;

import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public Integer getCount(HttpServletRequest request){
        Integer notificationCount = null;
        var session = request.getSession(false);
        if (session != null){
            //session not created
            var s = RequestContextHolder.currentRequestAttributes().getSessionId();
            notificationCount = notificationRepository.countBySessionIdAndSeenIsFalse(s);
        };
        return notificationCount;
    }
}
