package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerBoxDTO;
import cl.hccr.beermachine.domain.BeerItem;
import cl.hccr.beermachine.domain.BeerItemDTO;
import cl.hccr.beermachine.exceptions.BeerItemNotFoundException;
import cl.hccr.beermachine.exceptions.IdAlreadyExistException;
import cl.hccr.beermachine.repository.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public BeerItemDTO createBeerItem(BeerItemDTO newBeerItemRequest) {
        // Se verifica que el ID de cerveza que se intenta crear no exista, de lo contrario arroja excepcion IdAlreadyExistException
        if(beerRepository.existsById(newBeerItemRequest.getId()))
            throw new IdAlreadyExistException();

        //Se crea BeerItem en base al BeerItemDTO
        BeerItem beerItem = new BeerItem(newBeerItemRequest.getId(),newBeerItemRequest.getName(),
                newBeerItemRequest.getBrewery(),newBeerItemRequest.getCountry(),newBeerItemRequest.getPrice(),newBeerItemRequest.getCurrency());

        //Se guarda en Bd la nueva cerveza probada por Bender
        beerItem = beerRepository.save(beerItem);


        return new BeerItemDTO(beerItem.getId(),beerItem.getName(),beerItem.getBrewery(),
                beerItem.getCountry(),beerItem.getPrice(),beerItem.getCurrency());
    }

    @Override
    public BeerItemDTO getBeerItem(int id) {
        //Se busca directamente en BD por el ID de cerveza, metodo devuelve un Optional, el cual se verifica y en caso de no obtener resultado
        //de búsqueda se lanza la excepción BeerItemNotFoundException
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
        //Se obtienen todas las cervezas desde la BD
        List<BeerItem> beerItemList = beerRepository.findAll();

        //Se convierte la lista BeerItem a BeerItemDTO
        return beerItemList.stream().map(beerItem ->
                new BeerItemDTO(beerItem.getId(),beerItem.getName(),beerItem.getBrewery(),beerItem.getCountry(),beerItem.getPrice(),beerItem.getCurrency()))
                .collect(Collectors.toList());
    }


    @Override
    public BeerBoxDTO getBoxPrice(int idCerveza, String currency, int quantity) {
        BeerItemDTO beerItemDTO = getBeerItem(idCerveza);
        //Se verifica si la currency solicitada es la misma con la cual se creo la cerveza en la BD o si no se ingresó una currency, en
        // ese caso se utiliza la misma currency de la cerveza y se devuelve la cantidad * el precio
        if(beerItemDTO.getCurrency().equals(currency) || StringUtils.isEmpty(currency)){
            return new BeerBoxDTO(quantity*beerItemDTO.getPrice());
        }

        double convertionRate = currencyService.getConvertionRate(beerItemDTO.getCurrency(),currency);
        return new BeerBoxDTO(quantity * beerItemDTO.getPrice() * convertionRate);

    }
}
