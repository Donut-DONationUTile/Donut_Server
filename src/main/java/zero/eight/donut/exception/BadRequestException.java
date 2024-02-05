package zero.eight.donut.exception;

public class BadRequestException extends ApiException{
    public BadRequestException(Error error) {
        super(error);
    }
}
