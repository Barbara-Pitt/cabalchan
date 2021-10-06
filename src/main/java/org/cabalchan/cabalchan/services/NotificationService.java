package org.cabalchan.cabalchan.services;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.cabalchan.cabalchan.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public Integer getCount(Optional<Cookie> c){
        Integer notificationCount = null;
        if (c.isPresent()){
            notificationCount = notificationRepository.countByCabalUUIDAndSeenIsFalse(c.get().getValue());
        };
        return notificationCount;
    }
}
