package cl.hccr.beermachine;

import cl.hccr.beermachine.domain.BeerItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FalabellaBeerMachineIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getBeer() {
        ResponseEntity<List<BeerItem>> response = restTemplate.exchange("/beers",HttpMethod.GET,null, new ParameterizedTypeReference<List<BeerItem>>(){});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
