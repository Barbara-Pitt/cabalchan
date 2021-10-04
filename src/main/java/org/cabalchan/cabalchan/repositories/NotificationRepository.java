package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, BigInteger>{
    List<Notification> findBySessionIdOrderByIdDesc(String sessionId);
    Integer countBySessionIdAndSeenIsFalse(String sessionId);

    @Query("select n from Notification n where n.sessionId=:sessionId order by n.seen ASC, n.id DESC")
    Page<Notification> latestNotifications(String sessionId, Pageable pageable);

    @Query("select n from Notification n where n.createDate < :oldDate")
    List<Notification> oldNotifications(LocalDateTime oldDate);

    List<Notification> findByEntry(Entry entry);
}