package digital.patron.PatronMembers.repository.BusinessInformation;

import digital.patron.PatronMembers.domain.BusinessInformation.BusinessLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessLicenseRepository extends JpaRepository<BusinessLicense, Long> {
}
