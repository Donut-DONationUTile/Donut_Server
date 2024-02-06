package zero.eight.donut.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundException extends ApiException {
    public NotFoundException(Error error) {
        super(error);
    }
}