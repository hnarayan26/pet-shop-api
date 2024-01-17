package com.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {

    // 1. Data that should be returned by the endpoint
    private String name;
    private Integer age;
    private String species;
    private Double price;
}
