package digital.patron.PatronMembers.repository.MontlySubscritpion;

import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscriptionSales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlySubscriptionSalesRepository extends JpaRepository<MonthlySubscriptionSales, Long> {
}
