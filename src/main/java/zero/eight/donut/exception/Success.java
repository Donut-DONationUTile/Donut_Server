package zero.eight.donut.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {

    // Default
    SUCCESS(HttpStatus.OK, "Request successfully processed"),

    // 200 OK SUCCESS
    MYPAGE_RECEIVER_SUCCESS(HttpStatus.OK, "Get request for receiver's info completed successfully"),
    MYPAGE_GIVER_SUCCESS(HttpStatus.OK, "Get request for giver's info completed successfully"),
    GET_WALLET_SUCCESS(HttpStatus.OK, "Success in getting wallet info"),
    HOME_GIVER_SUCCESS(HttpStatus.OK, "Get request for giver's home info completed successfully"),
    HOME_RECEIVER_SUCCESS(HttpStatus.OK, "Get request for receiver's home info completed successfully"),
    HOME_RECEIVER_BOX_SUCCESS(HttpStatus.OK, "Get request for receiver's giftbox info completed successfully"),
    HOME_RECEIVER_GIFT_SUCCESS(HttpStatus.OK, "Get request for receiver's gift info completed successfully "),
    SET_REDIS_KEY_SUCCESS(HttpStatus.OK, "Redis Key is registered successfully"),
    GET_REDIS_KEY_SUCCESS(HttpStatus.OK, "Redis key searched successfully"),
    GET_RANKING_BY_PRICE_SUCCESS(HttpStatus.OK, "Successful donation price ranking inquiry"),
    GET_RANKING_BY_NUMBER_SUCCESS(HttpStatus.OK, "Successful donation number ranking inquiry"),
    GET_HISTORY_GIVER_DONATIONLIST_SUCCESS(HttpStatus.OK, "Success in getting donation history list"),
    GET_HISTORY_GIVER_DONATION_SUCCESS(HttpStatus.OK, " Success in getting a donation history info"),
    GET_HISTORY_RECEIVER_BENEFIT_SUCCESS(HttpStatus.OK, " Success in getting benefit history list"),

    //201 CREATED SUCCESS
    CREATE_FCM_TOKEN_SUCCESS(HttpStatus.CREATED, "Successfully update fcmToken"),
    SEND_MESSAGE_SUCCESS(HttpStatus.CREATED, "Send a message successfully"),
    UPLOAD_GIFT_SUCCESS(HttpStatus.CREATED, "Successfully upload gifticon"),
    ASSIGN_BENEFIT_SUCCESS(HttpStatus.CREATED, "Successfully assigned benefits"),
    DONATE_GIFT_SUCCESS(HttpStatus.CREATED, "Successfully donate gift"),
    CREATE_REPORT_SUCCESS(HttpStatus.CREATED, "Your report is successfully registered"),
    SIGN_IN_SUCCESS(HttpStatus.CREATED, "Sign in successfully"),
    SIGN_UP_SUCCESS(HttpStatus.CREATED, "Successfully signed up"),

    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}