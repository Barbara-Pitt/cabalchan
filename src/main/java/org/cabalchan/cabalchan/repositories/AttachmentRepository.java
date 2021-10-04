package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;

import org.cabalchan.cabalchan.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, BigInteger>{

}
