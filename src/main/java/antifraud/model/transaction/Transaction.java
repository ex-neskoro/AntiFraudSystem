package antifraud.model.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@JsonPropertyOrder({"transactionId", "amount", "ip", "number", "region", "date", "result", "feedback"})
public class Transaction {
    @JsonProperty("transactionId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Min(1)
    private long amount;
    @JsonProperty("result")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @JsonProperty("region")
    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private CountryCode countryCode;
    @Column(name = "date")
    @JsonProperty("date")
    private LocalDateTime dateTime;

    @Column(name = "ip", length = 20)
    private String ip;

    @Column(name = "number")
    private String number;

    @Column(name = "feedback")
    @Enumerated(EnumType.STRING)
    private TransactionStatus feedback;

    public Transaction() {
    }

    public Transaction(TransactionDTO dto) {
        amount = dto.getAmount();
        ip = dto.getIp();
        number = dto.getNumber();
        countryCode = dto.getCountryCode();
        dateTime = dto.getDateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TransactionStatus getFeedback() {
        return feedback;
    }

    @JsonProperty("feedback")
    public Object getFeedbackForJson() {
        return feedback == null ? "" : feedback;
    }

    public void setFeedback(TransactionStatus feedback) {
        this.feedback = feedback;
    }
}
