package org.cabalchan.cabalchan.entities;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name="categories",uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @NotBlank
    @Column(name="title", unique = true)
    private String title;

    @NotBlank
    private String subtitle;
}

