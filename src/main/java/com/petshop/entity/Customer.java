package com.petshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@NoArgsConstructor
@Data
public class Customer {

    @Id
    private Integer id;

    private String title;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastName;

    private String telephone;

    @Column(nullable = false)
    private String email;
}
