package antifraud.model.ip;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "ips",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"ip"}))
public class IP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Pattern(regexp = "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}", message = "IP does not match pattern")
    private String ip;

    public IP(Long id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public IP() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
