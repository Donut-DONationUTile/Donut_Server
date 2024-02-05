package zero.eight.donut.exception;

public class ConflictException extends ApiException{
    public ConflictException(Error error){
    super(error);
}
}
