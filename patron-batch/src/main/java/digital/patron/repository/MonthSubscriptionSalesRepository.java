package digital.patron.repository;

import digital.patron.domain.MonthSubscriptionSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthSubscriptionSalesRepository extends JpaRepository<MonthSubscriptionSales, Long> {
}
