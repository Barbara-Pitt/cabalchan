package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;

import org.cabalchan.cabalchan.entities.Appeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppealRepository extends JpaRepository<Appeal, BigInteger>{
    @Query(value = "select count(*) from Appeal a where a.appealStatus is null")
    Integer activeAppealCount();

    @Query("select a from Appeal a where a.appealStatus is null")
    Page<Appeal> latestActiveAppeals(Pageable pageable);
}
