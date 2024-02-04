package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import zero.eight.donut.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Receiver extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiver_id")
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String password;

    //신고
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Report> reportList = new ArrayList<>();

    //메세지
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Message> messagesList = new ArrayList<>();

    //수혜 정보
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private  List<Benefit> benefitList = new ArrayList<>();
}
