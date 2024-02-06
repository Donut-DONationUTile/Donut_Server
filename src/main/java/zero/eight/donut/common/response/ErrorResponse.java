package zero.eight.donut.common.response;

import zero.eight.donut.exception.Error;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final boolean success;
    private final String message;

    public static ErrorResponse error(Error error) {
        return new ErrorResponse(error.getHttpStatusCode(), error.getHttpStatus().is2xxSuccessful(), error.getMessage());
    }


    public static ErrorResponse error(Error error, String message) {
        return new ErrorResponse(error.getHttpStatusCode(), error.getHttpStatus().is2xxSuccessful(), message);
    }
}