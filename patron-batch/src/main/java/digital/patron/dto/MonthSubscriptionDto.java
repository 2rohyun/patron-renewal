package digital.patron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthSubscriptionDto {
    private BigDecimal totalAmount;
    private BigDecimal totalAmountExceptFee;
}
