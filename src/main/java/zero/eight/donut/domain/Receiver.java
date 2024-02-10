package zero.eight.donut.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import zero.eight.donut.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Receiver extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiver_id")
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 500)
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

    //꾸러미
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private  List<Giftbox> giftboxList = new ArrayList<>();

    /**
     * 비밀번호를 암호화
     * @param passwordEncoder 암호화 할 인코더 클래스
     * @return 변경된 유저 Entity
     */
    public Receiver encryptPassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
        return this;
    }

    /**
     * 비밀번호 확인
     * @param plainPassword 암호화 이전의 비밀번호
     * @param passwordEncoder 암호화에 사용된 클래스
     * @return true | false
     */
    public boolean verifyPassword(String password, PasswordEncoder encoder) {
        return encoder.matches(password, this.password);
    }
}
