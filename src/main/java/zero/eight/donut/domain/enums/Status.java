package zero.eight.donut.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
    REPORTED("REPORTED"),
    UNUSED("UNUSED"),
    USED("USED");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}