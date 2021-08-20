package digital.patron.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Users {
    private Long id;
    private String firstName;
    private String lastName;
}
