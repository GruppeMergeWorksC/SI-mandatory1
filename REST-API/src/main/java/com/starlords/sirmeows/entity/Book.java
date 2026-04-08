package com.starlords.sirmeows.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    private Integer publishingYear;
}
