package digital.patron.PatronMembers.repository.AccountInformation;

import digital.patron.PatronMembers.domain.MonthlySubscription.AccountInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountInformationRepository extends JpaRepository<AccountInformation,Long> {
}
