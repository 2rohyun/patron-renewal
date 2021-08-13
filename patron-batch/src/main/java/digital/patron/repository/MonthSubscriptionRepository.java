package digital.patron.repository;

import digital.patron.domain.MonthSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthSubscriptionRepository extends JpaRepository<MonthSubscription, Long> {
}
