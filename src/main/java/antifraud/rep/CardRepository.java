package antifraud.rep;

import antifraud.model.card.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {

    boolean existsByNumber(String number);

    Optional<Card> findByNumber(String number);
}
