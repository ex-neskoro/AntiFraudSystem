package antifraud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Transaction already have feedback")
public class TransactionAlreadyHaveFeedbackException extends RuntimeException {
}
