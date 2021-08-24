package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String status;

    private LocalDate createTime;

    private LocalDate birth;

    private String nationality;

    private String gender;

    private BigDecimal distributionRatio;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MonthSubscription monthSubscription;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AccountInfo accountInfo;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Artwork> artworks = new ArrayList<>();

}
