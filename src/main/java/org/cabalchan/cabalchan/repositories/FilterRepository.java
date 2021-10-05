package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;

import org.cabalchan.cabalchan.entities.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, BigInteger>{
}

