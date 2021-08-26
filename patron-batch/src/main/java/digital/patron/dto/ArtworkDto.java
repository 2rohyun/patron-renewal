package digital.patron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkDto {

    private int totalNumberOfViews;

    private int totalViewsExcludingThisMonth;

}
