package org.cabalchan.cabalchan.entities;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="entries")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Entry parent;

    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
    private List<Entry> replies = new ArrayList<Entry>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flag_id")
    private Flag flag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="filter_id")
    private Filter filter;

    @OneToOne(mappedBy = "entry",fetch = FetchType.LAZY)
    private Attachment attachment;

    @NotBlank
    private String ipaddr;

    @NotBlank
    @Column(name="cabaluuid")
    private String cabalUUID;

    private String comment;

    @NotNull
    @Column(name = "create_dt", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE NOT NULL")
    private LocalDateTime createDate;
}
