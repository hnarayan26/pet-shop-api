package com.rideways.petshopapi

import com.petshop.controller.PetShopController
import com.petshop.dto.PetDto
import com.petshop.service.PetShopService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Subject

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PetShopControllerTest extends Specification implements PetTrait {
    //Define static values outside the test
    def petShopService = Mock PetShopService
    @Subject  //Do we still need this?
    def petShopController
    MockMvc mockMvc
    String GET_ALL_PETS_URL = "/pets"

    @Value("classpath:getPetsResponse.json") Resource getPetsResponse

    def setup() {
        petShopController = new PetShopController(petShopService)
        mockMvc = MockMvcBuilders.standaloneSetup(petShopController)
                .build()
    }

    private List<PetDto> createPetsResponse() {
        List<PetDto> pets = new ArrayList<>();
        PetDto pet1 = new PetDto("Luna", Integer.valueOf(1), "cat", Double.valueOf(100))
        PetDto pet2 = new PetDto("Latte", Integer.valueOf(2), "dog", Double.valueOf(150))

        pets.add(pet1)
        pets.add(pet2)

        return pets
    }

    def "should return successful response given list of pets is returned by the service"() {
        given:
//        petShopService.getAllPets() >> [petTrait()] //why is this not working?
        def response = createPetsResponse();
        petShopService.getAllPets() >> response
        def expectedResponse = "[{\"name\":\"Luna\",\"age\":1,\"species\":\"cat\",\"price\":100.0},{\"name\":\"Latte\",\"age\":2,\"species\":\"dog\",\"price\":150.0}]"

        when:
        //call controller getAllPets endpoint
//        def result = petShopController.getAllPets()  //Why can't we call the controller this way? -> This way we are directly calling the controller and not the endpoint
        def result = mockMvc.perform(get(GET_ALL_PETS_URL)) //mockMvc is used to called the endPoints

        then:
        result.andExpect(status().is(200)).andExpect(content().string(expectedResponse))

    }

    def "should return successful response given PetDto is returned by the service"() {
        given:
        def singlePetResponse = createPetsResponse()[0];
        petShopService.getPetById(1) >> singlePetResponse
//        petShopService.getPetById(2) >> createPetsResponse()[1]
        def expectedResponse = "{\"name\":\"Luna\",\"age\":1,\"species\":\"cat\",\"price\":100.0}"

        when:
        def result = mockMvc.perform(get("/pet/2"))

        then:
        result.andExpect(status().is(200)).andExpect(content().string(expectedResponse))
    }
}