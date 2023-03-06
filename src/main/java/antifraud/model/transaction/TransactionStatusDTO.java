package antifraud.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionStatusDTO {
    @JsonProperty("result")
    private TransactionStatus result;
    @JsonProperty("info")
    private String info;

    public TransactionStatusDTO() {
    }

    public TransactionStatus getResult() {
        return result;
    }

    public void setResult(TransactionStatus result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void addToInfo(String reason) {
        if (info != null) {
            info = info + reason + ", ";
        } else {
            info = reason + ", ";
        }
    }

    public void cleanInfo() {
        if (info != null) {
            info = info.strip().substring(0, info.length() - 2);
        } else {
            info = "none";
        }
    }
}
