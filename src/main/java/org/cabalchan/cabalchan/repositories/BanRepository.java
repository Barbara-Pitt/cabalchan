package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import org.cabalchan.cabalchan.entities.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BanRepository extends JpaRepository<Ban, BigInteger>{

    @Query(value = "select count(*) from Ban b where b.expirationDate > :today and b.ipaddr = :ipaddr")
    Integer activeBans(String ipaddr, LocalDateTime today);

    @Query(value = "select b from Ban b where b.expirationDate > :today and b.ipaddr = :ipaddr")
    List<Ban> currentBans(String ipaddr, LocalDateTime today);
}
