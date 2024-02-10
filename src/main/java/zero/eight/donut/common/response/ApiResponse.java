package zero.eight.donut.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;


@Getter
@ToString
@AllArgsConstructor
public class ApiResponse<T> {

    private final int statusCode;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(
                Success.SUCCESS.getHttpStatusCode(),
                Success.SUCCESS.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> success(Success success) {
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> success(Success success, T data) {
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                Success.SUCCESS.getHttpStatusCode(),
                Success.SUCCESS.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> failure(Error error) {
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                error.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> failure(Error error, String message) {
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                message,
                null
        );
    }

    public static <T> ApiResponse<T> failure(Error error, T data) {
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                error.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> failure(T data) {
        return new ApiResponse<>(
                Error.ERROR.getHttpStatusCode(),
                Error.ERROR.getMessage(),
                data
        );
    }
}
