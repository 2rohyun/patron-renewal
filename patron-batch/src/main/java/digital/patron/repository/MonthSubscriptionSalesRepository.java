package digital.patron.repository;

import digital.patron.domain.MonthSubscriptionSales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthSubscriptionSalesRepository extends JpaRepository<MonthSubscriptionSales, Long> {
}
