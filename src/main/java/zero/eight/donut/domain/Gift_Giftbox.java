package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gift_Giftbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_giftbox_id")
    private Long id;

    @OneToOne(mappedBy = "gift_giftbox")
    private Gift gift;

    @ManyToOne
    @JoinColumn(name = "giftbox_id")
    private Giftbox giftbox;
}
