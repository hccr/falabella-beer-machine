package cl.hccr.beermachine.service;

import cl.hccr.beermachine.domain.BeerBoxDTO;
import cl.hccr.beermachine.domain.BeerItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class BeerBoxServiceImp implements BeerBoxService {


    private RestTemplate restTemplate;

    private BeerService beerService;

    //@Value("${com.currencylayer.api.access_key}")
    private final String ACCESS_KEY = "20bccb12e83335e8122cb2c52dee3276";

    //@Value("${com.currencylayer.api.host}")
    private final String HOST = "http://api.currencylayer.com";



    public BeerBoxServiceImp(BeerService beerService) {
        this.restTemplate = new RestTemplate();
        this.beerService = beerService;
    }

    @Override
    public BeerBoxDTO getBoxPrice(int idCerveza, String currency, int quantity) {
        BeerItemDTO beerItemDTO = beerService.getBeerItem(idCerveza);
        if(beerItemDTO.getCurrency().equals(currency) || StringUtils.isEmpty(currency)){
            return new BeerBoxDTO(quantity*beerItemDTO.getPrice());
        }
       UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(HOST)
                .path("/live")
                .queryParam("access_key", ACCESS_KEY)
                .queryParam("currencies", "USD,CLP,EUR");

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","PostmanRuntime/7.21.0");
        HttpEntity<?> request = new HttpEntity<>(headers);

      try{
          ResponseEntity<CurrencyResponse> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,request, CurrencyResponse.class);
          if(response.getStatusCode().is2xxSuccessful()){
              CurrencyResponse currencyResponse = response.getBody();
              double monedaOrigen = currencyResponse.getQuotes().get(currencyResponse.source+beerItemDTO.getCurrency());
              double monedaDestino = currencyResponse.getQuotes().get(currencyResponse.source+currency);


              return new BeerBoxDTO(quantity * monedaDestino / monedaOrigen);
          }

      }catch (Exception e){
          e.printStackTrace();
      }

        return new BeerBoxDTO(0.0);
    }

    static class CurrencyResponse{

        private boolean success;
        private String source;
        private Map<String, Double> quotes;

        public CurrencyResponse() {
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Map<String, Double> getQuotes() {
            return quotes;
        }

        public void setQuotes(Map<String, Double> quotes) {
            this.quotes = quotes;
        }
    }
}
