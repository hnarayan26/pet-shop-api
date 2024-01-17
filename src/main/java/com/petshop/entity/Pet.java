package com.petshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "pet")
@NoArgsConstructor
@Data
public class Pet {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    private int age;

    @Column(nullable = false)
    private String species;

    @CreationTimestamp
    private LocalDate addedOn;

    @Column(nullable = false)
    private Double price;
}
