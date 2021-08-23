package digital.patron.PatronMembers.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMemberDto {
    @NotBlank
    private List<Long> Id;

    private List<String> email;

    private List<String> name;

    private List<String> status;

    private List<LocalDateTime> create_time;

    private List<String> public_wallet;


}
