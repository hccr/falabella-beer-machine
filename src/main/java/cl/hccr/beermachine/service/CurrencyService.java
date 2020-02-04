package cl.hccr.beermachine.service;

public interface CurrencyService {
    double getConvertionRate(String monedaOrigen, String monedaDestino);
}
