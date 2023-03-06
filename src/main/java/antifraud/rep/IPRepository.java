package antifraud.rep;

import antifraud.model.ip.IP;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPRepository extends CrudRepository<IP, Long> {
    boolean existsByIp(String ip);
    Optional<IP> findByIp(String ip);

}
