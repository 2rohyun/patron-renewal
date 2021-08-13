package digital.patron.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String intro;

    private String size;

    private String year;

    private String where;

    private boolean approve;

    private boolean showing;

    private boolean chargeFree;

    private int numberOfLikes;

    private int numberOfViews;

    private LocalDate createTime;

    private LocalDate updateTime;

    private LocalDateTime registeredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private SaleMember saleMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessMember businessMember;

    public void setBusinessMember(BusinessMember businessMember) { this.businessMember = businessMember; }
    public void setSaleMember(SaleMember saleMember) { this.saleMember = saleMember; }
}
