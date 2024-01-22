package service;

import com.petshop.PetShopApiApplication;
import com.petshop.dto.PetDto;
import com.petshop.service.PetShopService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes= PetShopApiApplication.class)
public class PetShopServiceIT {

    // Makes sure when creating the instance, all the dependencies are automatically injected instead of doing the below
    // PetRepository petRepository;
    // PetShopService petShopService = new PetShopService(petRepository);
    @Autowired
    private PetShopService petShopService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @AfterEach
    public void cleanUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,
                "pet"
        );
    }

    @Test
    @Sql("classpath:petShop.sql")
    void shouldReturnListOfPets_givenPetsExistInDatabase() {
        List<PetDto> expectedPets = List.of(PetDto.builder()
                .name("Fluffy")
                .age(3)
                .species("Cat")
                .price(50.00)
                .build(),
            PetDto.builder()
                .name("Buddy")
                .age(2)
                .species("Dog")
                .price(75.00)
                .build(),
            PetDto.builder()
                .name("Whiskers")
                .age(1)
                .species("Hamster")
                .price(20.00)
                .build());

        List<PetDto> pets = petShopService.getAllPets();

        assertThat(pets).containsAll(expectedPets);
    }
}
