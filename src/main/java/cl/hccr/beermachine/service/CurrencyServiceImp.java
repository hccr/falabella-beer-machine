package cl.hccr.beermachine.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class CurrencyServiceImp implements CurrencyService {
    @Value("${com.currencylayer.api.access_key}")
    private String ACCESS_KEY ;

    @Value("${com.currencylayer.api.host}")
    private String HOST ;

    private RestTemplate restTemplate = new RestTemplate();


    @Override
    public double getConvertionRate(String monedaOrigen, String monedaDestino) {
        //Dado las limitantes de la capa gratuita de currencylayer.com, solo podemos traer los valores en base a dolar, por lo tanto si
        //necesitamos convertir desde y hacia monedas distintas de dolar debemos hacer un calculo adicional.
        //Se solicita el cambio DOLAR a moneda origen y DOLAR a moneda destino, luego se divide moneda destino / moneda origen y se obtiene
        //la tasa de conversion.


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(HOST)
                .path("/live")
                .queryParam("access_key", ACCESS_KEY)
                .queryParam("currencies", "USD,"+monedaDestino+","+monedaOrigen);

        HttpHeaders headers = new HttpHeaders();

        //Se agrega este header User-Agent ya que al realizar la petición sin este arroja un error Forbidden 403, probablemente sea una
        //restriccion de la api de currecylayer

        headers.set("User-Agent","PostmanRuntime/7.21.0");

        HttpEntity<?> request = new HttpEntity<>(headers);

        try{
            ResponseEntity<CurrencyResponse> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,request, CurrencyResponse.class);
            if(response.getStatusCode().is2xxSuccessful()){
                CurrencyResponse currencyResponse = response.getBody();
                double rateOrigen = currencyResponse.getQuotes().get(currencyResponse.getSource()+monedaOrigen);
                double rateDestino = currencyResponse.getQuotes().get(currencyResponse.getSource()+monedaDestino);
                return rateDestino / rateOrigen;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
    //Se crea esta clase para obtener los datos ya mapeados desde el api de currencylayer.com
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
