package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zero.eight.donut.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Giver extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giver_id")
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(length = 320)
    private String email;

    //기프티콘
    @OneToMany(mappedBy = "giver", fetch = FetchType.LAZY)
    private List<Gift> giftList = new ArrayList<>();

    //기부 정보
    @OneToOne(fetch = FetchType.LAZY)
    private Donation donation;

    //신고
    @OneToMany(mappedBy = "giver", fetch = FetchType.LAZY)
    private List<Report> reportList = new ArrayList<>();
}

