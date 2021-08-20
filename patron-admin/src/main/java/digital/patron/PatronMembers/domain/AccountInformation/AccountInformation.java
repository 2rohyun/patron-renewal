package digital.patron.PatronMembers.domain.MonthlySubscription;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class AccountInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(length = 300)
    private String public_wallet;
    @Column(length = 20)
    private String bank;
    @Column(length = 100)
    private String number;
    @Column(length = 500)
    private String bank_book;

    protected AccountInformation(){}


    public AccountInformation(String public_wallet, String bank, String number, String bank_book) {
        this.public_wallet = public_wallet;
        this.bank = bank;
        this.number = number;
        this.bank_book = bank_book;
    }
}
