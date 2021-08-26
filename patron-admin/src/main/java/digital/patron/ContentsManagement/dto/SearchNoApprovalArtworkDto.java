package digital.patron.ContentsManagement.dto;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchNoApprovalArtworkDto {
    @NotBlank
    private List<Long> Id;

    private List<String> email;

    private List<String> ArtworkName;

    private List<String> Thumbnail;

    private List<String> ArtistName;

    private List<LocalDateTime> registration_date;


}
