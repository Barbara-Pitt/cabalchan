package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import org.cabalchan.cabalchan.entities.Flag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlagRepository extends JpaRepository<Flag, BigInteger>{
    Flag findByFilename(String filename);
}
