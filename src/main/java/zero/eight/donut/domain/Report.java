package zero.eight.donut.domain;

import zero.eight.donut.common.domain.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    //기부자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private Giver giver;

    //수혜자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;

    //기프티콘
    @OneToOne(mappedBy = "report")
    private Gift gift;

}
