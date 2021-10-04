package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.cabalchan.cabalchan.entities.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntryRepository extends JpaRepository<Entry, BigInteger>{
    @Query(value = "select e from Entry e where e.parent IS NULL order by e.createDate DESC")
    Page<Entry> threadsPage(Pageable pageable);

    @Query(value = "select e from Entry e where e.parent=:parentEntry order by e.id DESC")
    Page<Entry> entriesPage(Entry parentEntry, Pageable pageable);

    @Query(value = "select count(*) from Entry e where e.createDate > :oldDate")
    Integer activePPH(LocalDateTime oldDate);

    @Query(value = "select count(distinct e.ipaddr) from Entry e where e.createDate > :oldDate")
    Integer activeAddressCount(LocalDateTime oldDate);

    @Query(value = "select e from Entry e where e.ipaddr=:ipaddr order by e.id DESC")
    Page<Entry> postHistory(String ipaddr, Pageable pageable);
}
