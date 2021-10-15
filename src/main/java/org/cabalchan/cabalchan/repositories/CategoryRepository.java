package org.cabalchan.cabalchan.repositories;

import java.math.BigInteger;
import java.util.Optional;

import org.cabalchan.cabalchan.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, BigInteger>{
    Optional<Category> findByTitle(String title);
}

