package cl.hccr.beermachine.repository;

import cl.hccr.beermachine.domain.BeerItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<BeerItem, Integer> {

}
