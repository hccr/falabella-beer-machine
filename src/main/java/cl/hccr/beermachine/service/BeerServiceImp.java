package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.domain.NewBeerItemRequestDTO;
import cl.hccr.beermachine.exceptions.BeerItemNotFoundException;
import cl.hccr.beermachine.exceptions.IdAlreadyExistException;
import cl.hccr.beermachine.repository.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerServiceImp implements BeerService {

    private BeerRepository beerRepository;

    public BeerServiceImp(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
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
}
