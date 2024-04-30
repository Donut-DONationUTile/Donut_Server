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

    public void updateToken(String token, Role role) {
        this.token = token;
        this.role = role;
    }
}
