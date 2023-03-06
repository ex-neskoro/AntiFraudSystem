package antifraud.rep;

import antifraud.model.transaction.CountryCode;
import antifraud.model.transaction.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByNumberOrderByIdAsc(String number);
    long countByCountryCode(CountryCode countryCode);

    @Query("select count(distinct (t.countryCode)) from Transaction t where t.dateTime between ?1 and ?2 and t.countryCode != ?3")
    int countRegionsFromLastHour(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, CountryCode code);

    @Query("select count(distinct (t.ip)) from Transaction t where t.dateTime between ?1 and ?2 and t.ip != ?3")
    int countIPsFromLastHourByNotIp(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, String ip);

    List<Transaction> findByOrderByIdAsc();
}
