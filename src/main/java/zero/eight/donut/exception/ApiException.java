package zero.eight.donut.exception;

import lombok.Getter;
@Getter
public class ApiException extends RuntimeException {
    private final Error error;
    public ApiException(Error error){
        super(error.getMessage());
        this.error = error;
    }
    public int getHttpStatus(){
        return error.getHttpStatusCode();
    }
}