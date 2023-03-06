package antifraud.model.card;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {
    @JsonProperty("number")
    private String number;

    public CardDTO() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
