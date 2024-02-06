package zero.eight.donut.exception;

public class UnauthorizedException extends ApiException{
    public UnauthorizedException(Error error) {
        super(error);
    }
}
