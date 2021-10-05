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
@Table(name="filters",uniqueConstraints = { 
    @UniqueConstraint(columnNames = { "cssclass" })
    ,@UniqueConstraint(columnNames = { "filtername" })
})
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @NotBlank
    @Column(name="cssclass", unique = true)
    private String cssclass;

    @NotBlank
    @Column(name="filtername", unique = true)
    private String filtername;
}
