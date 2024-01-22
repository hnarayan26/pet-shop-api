package com.petshop.service;

import com.petshop.repository.PetRepository;
import com.petshop.dto.PetDto;
import com.petshop.entity.Pet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PetShopService {

    private PetRepository petRepository;

    public List<PetDto> getAllPets() {
        // Call repository
        // Convert entity (Pet) to Dto (PetDto)
        // Use streams
        // Read about streams
        // In the petRepo we've defined it to use Pet entity so findAll expects us to return List<Pet> but in this service method
        // we've defined return type as List<PetDto> so need to do the conversion.

        return convertToList((List<Pet>) petRepository.findAll());
    }

    public PetDto getPetById(int id) {
        Optional<Pet> pet = petRepository.findById(id);
        PetDto petResponse = pet.map(elem -> new PetDto(elem.getName(), elem.getAge(), elem.getSpecies(), elem.getPrice())).orElse(null);
        if(petResponse == null) {
            throw new RuntimeException("No pet found");
        }
        return petResponse;
    }

    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    public void updatePet(Integer id, Pet newPet) {

        //How do we gain confidence that the update has been successful
        //To change from Optional data type to Pet or any custom data type we can use .stream()
        Pet existingPet = petRepository.findById(id).stream().findFirst().orElse(null);

        //Change to ternary statement
        if(existingPet != null) {
            if (newPet.getId() != null) {
                existingPet.setId(newPet.getId());
            }
            if (newPet.getName() != null) {
                existingPet.setName(newPet.getName());
            }
            if (newPet.getAge() != 0) {
                existingPet.setAge(newPet.getAge());
            }
            if (newPet.getSpecies() != null) {
                existingPet.setSpecies(newPet.getSpecies());
            }
            if (newPet.getAddedOn() != null) {
                existingPet.setAddedOn(newPet.getAddedOn());
            }
            if (newPet.getPrice() != null) {
                existingPet.setPrice(newPet.getPrice());
            }

            petRepository.save(existingPet);
        }

    }

    public void deletePet(int id) {
        //How do we gain confidence that the deletion has been successful, do we get a response from the database -> might add some logs around that, go check the database
        petRepository.deleteById(id);
    }

//    private List<PetDto> convertIterableToList(Iterable<Pet> pet) {
//       return StreamSupport.stream(pet.spliterator(), false)
//               .map(petItem -> {
//                   if(petItem != null) {
//                       return PetDto.builder()
//                               .name(petItem.getName())
//                               .age(petItem.getAge())
//                               .species(petItem.getSpecies())
//                               .price(petItem.getPrice())
//                               .build();
//                   }
//                   return new PetDto();
//               })
//               .collect(Collectors.toList());
//    }

    private List<PetDto> convertToList(List<Pet> pets) {
        return pets.stream()
                .map(petItem -> {
                    if (petItem != null) {
                        return PetDto.builder()
                                .name(petItem.getName())
                                .age(petItem.getAge())
                                .species(petItem.getSpecies())
                                .price(petItem.getPrice())
                                .build();
                    }
                    return new PetDto();
                })
                .collect(Collectors.toList());
    }
}
