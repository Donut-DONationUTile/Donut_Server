package zero.eight.donut.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
    REPORTED("reported"),
    UNUSED("unused"),
    USED("used");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}