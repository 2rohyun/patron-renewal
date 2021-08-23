package digital.patron.PatronMembers.repository.AccountInformation;

import digital.patron.PatronMembers.domain.MonthlySubscription.AccountInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInformationRepository extends JpaRepository<AccountInformation, Long> {
}
