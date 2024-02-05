package zero.eight.donut.common.response;

import lombok.*;
import zero.eight.donut.exception.Success;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {
    private final int status;
    private final boolean success;
    private final String message;
    private T data;

    public static SuccessResponse success(Success success) {
        return new SuccessResponse<>(success.getHttpStatusCode(), success.getHttpStatus().is2xxSuccessful(), success.getMessage());
    }


    public static <T> SuccessResponse<T> success(Success success, T data) {
        return new SuccessResponse<T>(success.getHttpStatusCode(),  success.getHttpStatus().is2xxSuccessful(),success.getMessage(), data);
    }
}