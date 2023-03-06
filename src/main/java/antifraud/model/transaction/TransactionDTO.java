package antifraud.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class TransactionDTO {

    @JsonProperty("amount")
    @Min(1)
    private long amount;
    @JsonProperty("ip")
    @NotEmpty
    private String ip;
    @JsonProperty("number")
    @NotEmpty
    private String number;
    @JsonProperty("region")
    private CountryCode countryCode;
    @JsonProperty("date")
    private LocalDateTime dateTime;

    public TransactionDTO() {
    }

    public TransactionDTO(long amount, String ip, String number) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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
}
