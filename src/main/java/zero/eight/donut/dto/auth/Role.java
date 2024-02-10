package zero.eight.donut.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_GIVER("ROLE_GIVER", "기부자"),
    ROLE_RECEIVER("ROLE_RECEIVER", "수혜자");

    private final String role;
    private final String title;
}
