package antifraud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.management.relation.RoleUnresolved;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User already exist")
public class UserAlreadyExistException extends RuntimeException {
}
