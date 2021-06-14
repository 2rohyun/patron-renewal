package digital.patron.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class AccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String publicWallet;

    @Column(nullable = false, length = 20)
    private String bank;

    @Column(nullable = false, length = 100)
    private String accountNumber;

    @Column(nullable = false, length = 500)
    private String bankbook;

}
