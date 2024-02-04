package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import zero.eight.donut.common.domain.BaseTimeEntity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Giver extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giver_id")
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String email;

}
