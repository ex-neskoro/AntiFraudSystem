package antifraud.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Feedback {
    @JsonProperty("transactionId")
    private long transactionId;
    @JsonProperty("feedback")
    private TransactionStatus status;

    public Feedback() {
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
