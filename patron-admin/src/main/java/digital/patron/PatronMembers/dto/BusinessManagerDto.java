package digital.patron.PatronMembers.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessManagerDto {

    private String email;

    private String name;

    private String phone_number;

    private String permission;

}
