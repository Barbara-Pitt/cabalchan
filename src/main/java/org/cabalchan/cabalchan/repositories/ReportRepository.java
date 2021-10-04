package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import java.util.List;

import org.cabalchan.cabalchan.entities.Entry;
import org.cabalchan.cabalchan.entities.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ReportRepository extends JpaRepository<Report, BigInteger>{
    List<Report> findByEntry(Entry entry);

    @Query(value = "select r from Report r order by Id DESC")
    Page<Report> reportsPage(Pageable pageable);
}
