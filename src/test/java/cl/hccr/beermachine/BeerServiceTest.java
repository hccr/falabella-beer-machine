package cl.hccr.beermachine;

import cl.hccr.beermachine.domain.BeerBoxDTO;
import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;
import cl.hccr.beermachine.exceptions.BeerItemNotFoundException;
import cl.hccr.beermachine.exceptions.IdAlreadyExistException;
import cl.hccr.beermachine.repository.BeerRepository;
import cl.hccr.beermachine.service.BeerService;
import cl.hccr.beermachine.service.BeerServiceImp;
import cl.hccr.beermachine.service.CurrencyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BeerServiceTest {

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private CurrencyService currencyService;

    private BeerService beerService;

    @BeforeEach
    void setUp(){
        beerService = new BeerServiceImp(beerRepository, currencyService);
    }

    @Test
    void getBeer_ShouldReturnBeerItemDTO(){
        given(beerRepository.findById(1)).willReturn(Optional.of(getBeerItem()));

        BeerItemDTO beerItemDTO = beerService.getBeerItem(1);
        assertThat(beerItemDTO.getId()).isEqualTo(1);
        assertThat(beerItemDTO.getName()).isEqualTo("Golden");
        assertThat(beerItemDTO.getBrewery()).isEqualTo("Kross");
        assertThat(beerItemDTO.getCountry()).isEqualTo("Chile");
        assertThat(beerItemDTO.getCurrency()).isEqualTo("EUR");
        assertThat(beerItemDTO.getPrice()).isEqualTo(1.5);

    }

    @Test
    void getBeer_ShouldThrowBeerNotFoundException(){
        given(beerRepository.findById(0)).willReturn(Optional.empty());

        assertThrows(BeerItemNotFoundException.class,()->beerService.getBeerItem(0));

    }

    @Test
    void getBeers_ShouldReturnBeerList(){
        given(beerRepository.findAll()).willReturn(Collections.singletonList(getBeerItem()));

        List<BeerItemDTO> beers = beerService.getBeers();
        assertThat(beers.size()).isEqualTo(1);
        assertThat(beers.get(0).getId()).isEqualTo(1);
        assertThat(beers.get(0).getName()).isEqualTo("Golden");
        assertThat(beers.get(0).getBrewery()).isEqualTo("Kross");
        assertThat(beers.get(0).getCountry()).isEqualTo("Chile");
        assertThat(beers.get(0).getCurrency()).isEqualTo("EUR");
        assertThat(beers.get(0).getPrice()).isEqualTo(1.5);
    }


    @Test
    void getBeerList_ShouldReturnEmptyBeerList(){
        given(beerRepository.findAll()).willReturn(Collections.emptyList());

        List<BeerItemDTO> beers = beerService.getBeers();
        assertThat(beers.isEmpty()).isEqualTo(true);
    }

    @Test
    void createBeer_ShouldReturnBeerItemDTO(){
        given(beerRepository.save(any(BeerItem.class))).willReturn(getBeerItem());
        BeerItemDTO beerItemDTO = beerService.createBeerItem(new NewBeerItemRequestDTO(1,"Golden","Kross","Chile",1.5,"EUR"));
        assertThat(beerItemDTO.getId()).isEqualTo(1);
        assertThat(beerItemDTO.getName()).isEqualTo("Golden");
        assertThat(beerItemDTO.getBrewery()).isEqualTo("Kross");
        assertThat(beerItemDTO.getCountry()).isEqualTo("Chile");
        assertThat(beerItemDTO.getCurrency()).isEqualTo("EUR");
        assertThat(beerItemDTO.getPrice()).isEqualTo(1.5);
    }

    @Test
    void createBeer_ShouldThrowIdAlreadyExistException(){
        given(beerRepository.existsById(1)).willReturn(true);

        assertThrows(IdAlreadyExistException.class,()->beerService.createBeerItem(new NewBeerItemRequestDTO(1,"Golden","Kross","Chile",1.5,"EUR")));

    }




    @Test
    void getBoxPrice_ShouldThrowBeerItemNotFoundException(){
        given(beerRepository.findById(0)).willReturn(Optional.empty());
        assertThrows(BeerItemNotFoundException.class,()->beerService.getBoxPrice(0,"CLP",6));
    }

    @Test
    void getBoxPrice_ShouldReturnBoxPriceDetails(){
        given(beerRepository.findById(1)).willReturn(Optional.of(new BeerItem(1,"Golden","Kross","Chile",1.5,"EUR")));
        BeerBoxDTO beerBoxDTO = beerService.getBoxPrice(1,"EUR",6);
        Assertions.assertThat(beerBoxDTO.getPriceTotal()).isEqualTo(6.0*1.5);
    }

    @Test
    void getBoxPriceWithDifferentCurrency_ShouldReturnBoxPriceDetails(){
        given(beerRepository.findById(1)).willReturn(Optional.of(new BeerItem(1,"Golden","Kross","Chile",1.5,"EUR")));
        given(currencyService.getConvertionRate("EUR","CLP")).willReturn(871.3);

        BeerBoxDTO beerBoxDTO = beerService.getBoxPrice(1,"CLP",6);


        Assertions.assertThat(Math.round(beerBoxDTO.getPriceTotal())).isEqualTo(Math.round(6*871.3*1.5));
    }




    private BeerItem getBeerItem(){
        return new BeerItem(1,"Golden","Kross","Chile",1.5,"EUR");
    }

}
