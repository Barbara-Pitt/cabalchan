package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.cabalchan.cabalchan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, BigInteger> {
    Optional<User> findUserByUsername(String u);

    @Query("select u from User u where u.staff=true")
    List<User> findStaff();
}