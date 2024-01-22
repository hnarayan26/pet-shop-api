package com.rideways.petshopapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.petshop.PetShopApiApplication
import com.petshop.controller.PetShopController
import com.petshop.dto.PetDto
import com.petshop.entity.Pet
import com.petshop.service.PetShopService
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.format.DateTimeFormatter
import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.http.MediaType.APPLICATION_JSON

@SpringBootTest(classes= PetShopApiApplication.class)
class PetShopControllerTest extends Specification {
    //Define static values outside the test
    def petShopService = Mock PetShopService
    def petShopController
    MockMvc mockMvc
    String GET_ALL_PETS_URL = "/pets"

    @Autowired ObjectMapper objectMapper
    @Value("classpath:getPetsResponse.json") Resource getPetsResponse

    def setup() {
        petShopController = new PetShopController(petShopService)
        mockMvc = MockMvcBuilders.standaloneSetup(petShopController).setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
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
        def response = createPetsResponse();
        petShopService.getAllPets() >> response
        def expectedResponse = "[{\"name\":\"Luna\",\"age\":1,\"species\":\"cat\",\"price\":100.0},{\"name\":\"Latte\",\"age\":2,\"species\":\"dog\",\"price\":150.0}]"

        when:
        //call controller getAllPets endpoint
        //def result = petShopController.getAllPets()  //Why can't we call the controller this way? -> This way we are directly calling the controller and not the endpoint
        def result = mockMvc.perform(get(GET_ALL_PETS_URL)) //mockMvc is used to called the endPoints

        then:
        result.andExpect(status().is(200)).andExpect(content().string(expectedResponse))

    }

    def "should return successful response given PetDto is returned by the service"() {
        given:
        def singlePetResponse = createPetsResponse()[0]
        petShopService.getPetById(1) >> singlePetResponse  //petShopService.getPetById(2) >> createPetsResponse()[1]
        def expectedResponse = "{\"name\":\"Luna\",\"age\":1,\"species\":\"cat\",\"price\":100.0}"

        when:
        def result = mockMvc.perform(get("/pet/1"))

        then:
        result.andExpect(status().is(200)).andExpect(content().string(expectedResponse))
    }

//    def "should throw exception given PetDto is NOT returned by the service"() {
//        given:
//        petShopService.getPetById(10) >> { throw new RuntimeException("No pet found")}
//
//        when:
//        def result = mockMvc.perform(get("/pet/10"))
//
//        then:
//        result.andExpect(status().isInternalServerError()).andExpect(content().string(containsString("No pet found")))
//    }

    def "should return pet with 201 status response  given pet is added successfully"() {
        Pet pet = new Pet(7, "Luna", 1, "cat", LocalDate.now(), 100.0)
        def requestBody = JsonOutput.toJson([
                id: 7,
                name: "Luna",
                age: 1,
                species: "cat",
                addedOn: pet.addedOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                price: 100.0
        ])

        given:
        petShopService.addPet(pet) >> pet

        when:
        def result = mockMvc.perform(post("/addpet").contentType(APPLICATION_JSON).content(requestBody))

        then:
        result.andExpect(status().isCreated()).andExpect(content().string(requestBody))
    }

    def "should return OK response on successful pet update"() {
        given:
        def id = 1
        def existingPet = new Pet(id, "Luna", 1, "cat", LocalDate.now(), 100.0)
        //Is this the right way of sending payload for updating just the name
        def newPet = new Pet(id, "Buddy", 1, "cat", LocalDate.now(), 100.0)
        def requestBody = JsonOutput.toJson([
                name: "Buddy"
        ])

        petShopService.updatePet(id, newPet) >> newPet

        when:
        def result = mockMvc.perform(put("/update/1").contentType(APPLICATION_JSON).content(requestBody))

        then:
        result.andExpect(status().isOk()).andExpect(content().string("Pet updated successfully"))

    }
}