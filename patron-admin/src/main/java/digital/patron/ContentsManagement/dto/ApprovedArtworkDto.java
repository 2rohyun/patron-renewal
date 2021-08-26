package digital.patron.ContentsManagement.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApprovedArtworkDto {
    @NotBlank
    private List<Long> Id;

    private List<String> code;

    private List<String> ArtworkName;

    private List<String> ArtistName;


}
