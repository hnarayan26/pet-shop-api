package com.rideways.petshopapi

import com.petshop.dto.PetDto

trait PetTrait {

    PetDto petDto(Map params = [:]) {
        def defaultParams = [
            name: "Casper",
            age: 7,
            species: "Dog",
            price: 189.36
        ]

        new PetDto(defaultParams + params)
    }

    //Look at DeclineMetricTrait
    //Create a trait for PetDto
}