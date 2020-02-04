package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerBoxDTO;
import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;
import cl.hccr.beermachine.exceptions.BeerItemNotFoundException;
import cl.hccr.beermachine.exceptions.IdAlreadyExistException;
import cl.hccr.beermachine.repository.BeerRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerServiceImp implements BeerService {

    private BeerRepository beerRepository;
    private CurrencyService currencyService;

    public BeerServiceImp(BeerRepository beerRepository, CurrencyService currencyService) {
        this.beerRepository = beerRepository;
        this.currencyService = currencyService;
    }


    @Override
    public BeerItemDTO createBeerItem(NewBeerItemRequestDTO newBeerItemRequest) {

        if(beerRepository.existsById(newBeerItemRequest.getId()))
            throw new IdAlreadyExistException();

        BeerItem beerItem = new BeerItem(newBeerItemRequest.getId(),newBeerItemRequest.getName(),
                newBeerItemRequest.getBrewery(),newBeerItemRequest.getCountry(),newBeerItemRequest.getPrice(),newBeerItemRequest.getCurrency());

        beerItem = beerRepository.save(beerItem);

        return new BeerItemDTO(beerItem.getId(),beerItem.getName(),beerItem.getBrewery(),
                beerItem.getCountry(),beerItem.getPrice(),beerItem.getCurrency());
    }

    @Override
    public BeerItemDTO getBeerItem(int id) {
        Optional<BeerItem> beerItemOptional = beerRepository.findById(id);
        if(beerItemOptional.isPresent()){
            return new BeerItemDTO(beerItemOptional.get().getId(),beerItemOptional.get().getName(),beerItemOptional.get().getBrewery(),
                    beerItemOptional.get().getCountry(),beerItemOptional.get().getPrice(),beerItemOptional.get().getCurrency());
        }else{
            throw new BeerItemNotFoundException();
        }
    }

    @Override
    public List<BeerItemDTO> getBeers() {
        List<BeerItem> beerItemList = beerRepository.findAll();

        return beerItemList.stream().map(beerItem ->
                new BeerItemDTO(beerItem.getId(),beerItem.getName(),beerItem.getBrewery(),beerItem.getCountry(),beerItem.getPrice(),beerItem.getCurrency()))
                .collect(Collectors.toList());
    }


    @Override
    public BeerBoxDTO getBoxPrice(int idCerveza, String currency, int quantity) {
        BeerItemDTO beerItemDTO = getBeerItem(idCerveza);
        if(beerItemDTO.getCurrency().equals(currency) || StringUtils.isEmpty(currency)){
            return new BeerBoxDTO(quantity*beerItemDTO.getPrice());
        }

        double convertionRate = currencyService.getConvertionRate(beerItemDTO.getCurrency(),currency);
        return new BeerBoxDTO(quantity * beerItemDTO.getPrice() * convertionRate);
        /*
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(HOST)
                .path("/live")
                .queryParam("access_key", ACCESS_KEY)
                .queryParam("currencies", "USD,CLP,EUR");

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","PostmanRuntime/7.21.0");
        HttpEntity<?> request = new HttpEntity<>(headers);

        try{
            ResponseEntity<BeerBoxServiceImp.CurrencyResponse> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,request, BeerBoxServiceImp.CurrencyResponse.class);
            if(response.getStatusCode().is2xxSuccessful()){
                BeerBoxServiceImp.CurrencyResponse currencyResponse = response.getBody();
                double monedaOrigen = currencyResponse.getQuotes().get(currencyResponse.source+beerItemDTO.getCurrency());
                double monedaDestino = currencyResponse.getQuotes().get(currencyResponse.source+currency);


                return new BeerBoxDTO(quantity * monedaDestino / monedaOrigen);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
*/
        //return new BeerBoxDTO(0.0);
    }
}
