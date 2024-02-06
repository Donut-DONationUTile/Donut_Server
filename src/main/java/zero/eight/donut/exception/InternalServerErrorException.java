package zero.eight.donut.exception;

public class InternalServerErrorException extends ApiException{
    public InternalServerErrorException(Error error) {
        super(error);
    }
}
