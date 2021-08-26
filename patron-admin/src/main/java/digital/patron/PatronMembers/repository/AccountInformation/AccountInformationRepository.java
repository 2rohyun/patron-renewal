package digital.patron.PatronMembers.repository.AccountInformation;

import digital.patron.PatronMembers.domain.MonthlySubscription.AccountInformation;
import digital.patron.PatronMembers.domain.AccountInformation.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInformationRepository extends JpaRepository<AccountInfo, Long> {
}
