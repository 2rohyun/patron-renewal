package digital.patron.domain;

import digital.patron.utils.BooleanToYNConverter;
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

    private String keep;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean approve;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean showing;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean chargeFree;

    private int numberOfLikes;

    private int numberOfViews;

    private int viewsExcludingThisMonth;

    private LocalDate createTime;

    private LocalDate updateTime;

    private LocalDateTime registeredAt;

    public void changeViewsExcludingThisMonth(int viewsExcludingThisMonth) {
        this.viewsExcludingThisMonth = viewsExcludingThisMonth;
    }

}
