package zero.eight.donut.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {


    // 200 OK SUCCESS
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공입니다"),

    //201 CREATED SUCCESS
    CREATE_GIFT_SUCCESS(HttpStatus.CREATED, "기프티콘이 성공적으로 등록되었습니다."),

    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}