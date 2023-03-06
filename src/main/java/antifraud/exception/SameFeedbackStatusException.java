package antifraud.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Transaction have the same status with feedback")
public class SameFeedbackStatusException extends RuntimeException {
}
