package zero.eight.donut.common.response;

import zero.eight.donut.exception.Error;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ErrorResponse {
    private final int code;
    private final String message;

    public static ErrorResponse error(Error error) {
        return new ErrorResponse(error.getHttpStatusCode(), error.getMessage());
    }


    public static ErrorResponse error(Error error, String message) {
        return new ErrorResponse(error.getHttpStatusCode(), message);
    }
}