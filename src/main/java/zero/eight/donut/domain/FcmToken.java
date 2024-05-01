package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zero.eight.donut.dto.auth.Role;

@Getter
@NoArgsConstructor
@Entity
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    private Role role;

    @Builder  // 롬복의 @Builder 애너테이션 적용
    public FcmToken(String token, Long memberId, Role role) {
        this.token = token;
        this.memberId = memberId;
        this.role = role;
    }

    public FcmToken updateToken(String token) {
        this.token = token;
        return this;
    }
}