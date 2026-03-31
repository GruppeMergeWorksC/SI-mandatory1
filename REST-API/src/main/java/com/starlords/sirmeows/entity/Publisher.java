package com.starlords.sirmeows.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Publisher {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
