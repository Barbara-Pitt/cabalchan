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
@Table(name="flags",uniqueConstraints = { 
    @UniqueConstraint(columnNames = { "filename" })
    ,@UniqueConstraint(columnNames = { "flagname" })
})
public class Flag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @NotBlank
    private String filetype;

    @NotBlank
    @Column(name="filename", unique = true)
    private String filename;

    @NotBlank
    @Column(name="flagname", unique = true)
    private String flagname;
}
