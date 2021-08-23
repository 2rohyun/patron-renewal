package digital.patron.PatronMembers.repository.MontlySubscritpion;

import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscriptionSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlySubscriptionSalesRepository extends JpaRepository<MonthlySubscriptionSales, Long> {
}
