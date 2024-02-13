package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DonationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donationInfo_id")
    private Long id;

    @Column
    private Long sum;

    @Column
    private Long count;

    @Column
    private Integer year;

    @Column
    private Integer month;

    public void updateSumCount(Long sum, Long count){
        this.sum = sum;
        this.count=count;
    }
}
