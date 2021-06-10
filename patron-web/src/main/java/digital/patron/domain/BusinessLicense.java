package digital.patron.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class BusinessLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String license;

    @Column(nullable = false, length = 100)
    private String registrationNumber;

}
