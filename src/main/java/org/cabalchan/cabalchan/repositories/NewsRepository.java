package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import org.cabalchan.cabalchan.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, BigInteger>{
    @Query(value = "select n from News n order by n.createDate DESC")
    Page<News> newsPage(Pageable pageable);
}
