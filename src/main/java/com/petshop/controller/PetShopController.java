package com.petshop.controller;

import com.petshop.dto.PetDto;
import com.petshop.entity.Pet;
import com.petshop.service.PetShopService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
1. Define the endpoint i.e. create a method
2. Declare the service that you are going to use
 */
@RestController
@AllArgsConstructor
public class PetShopController {

    private PetShopService petShopService;

    @GetMapping("/pets")
    public List<PetDto> getAllPets() {
        // Call service
        return petShopService.getAllPets();
    }

    @GetMapping("/pet/{id}")
    public PetDto getPetById(@PathVariable int id) {
        try {
            return petShopService.getPetById(id);
        } catch(Exception e) {
            throw new RuntimeException("Get by Id controller error", e);
        }
    }

    @PostMapping("/addpet")
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        Pet addedPet = petShopService.addPet(pet);
        return new ResponseEntity<>(addedPet, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePet(@PathVariable Integer id, @RequestBody Pet newPet) {
        petShopService.updatePet(id, newPet);
        return ResponseEntity.ok("Pet updated successfully");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletePet(@PathVariable int id) {
        petShopService.deletePet(id);
        return ResponseEntity.ok("Pet deleted successfully");
    }


}
