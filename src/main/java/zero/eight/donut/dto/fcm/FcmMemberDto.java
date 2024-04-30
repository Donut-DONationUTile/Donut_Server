package zero.eight.donut.dto.fcm;

import lombok.Builder;
import lombok.Getter;
import zero.eight.donut.dto.auth.Role;

@Getter
@Builder
public class FcmMemberDto {
    private Long id;
    private Role role;
}