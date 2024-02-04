package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Giftbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giftbox_id")
    private Long id;

    @Column
    private LocalDateTime assignedAt;

    @OneToMany(mappedBy = "giftbox", fetch = FetchType.LAZY)
    private List<Gift_Giftbox> gift_giftboxList = new ArrayList<>();

}
