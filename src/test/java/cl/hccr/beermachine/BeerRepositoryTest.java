package cl.hccr.beermachine;

import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void getBeer_ShouldReturnBeerItem(){
        BeerItem savedBeerItem = testEntityManager.persistAndFlush(getBeerItem());

        Optional<BeerItem> beerItemOptional =  beerRepository.findById(1);
        assertThat(beerItemOptional.isPresent()).isEqualTo(true);
        assertThat(savedBeerItem.getId()).isEqualTo(beerItemOptional.get().getId());
        assertThat(savedBeerItem.getName()).isEqualTo(beerItemOptional.get().getName());
        assertThat(savedBeerItem.getBrewery()).isEqualTo(beerItemOptional.get().getBrewery());
        assertThat(savedBeerItem.getCountry()).isEqualTo(beerItemOptional.get().getCountry());
        assertThat(savedBeerItem.getPrice()).isEqualTo(beerItemOptional.get().getPrice());
        assertThat(savedBeerItem.getCurrency()).isEqualTo(beerItemOptional.get().getCurrency());
    }



    private BeerItem getBeerItem(){
        return new BeerItem(1,"Golden","Kross","Chile",1.5,"EUR");
    }

}
