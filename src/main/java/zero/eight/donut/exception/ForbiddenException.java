package zero.eight.donut.exception;

public class ForbiddenException extends ApiException{
    public ForbiddenException(Error error) {
        super(error);
    }
}
