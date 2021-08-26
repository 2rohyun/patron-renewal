package digital.patron.PatronMembers.repository.BusinessInformation;

import digital.patron.PatronMembers.domain.BusinessInformation.BusinessManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessManagerRepository extends JpaRepository<BusinessManager, Long> {
    @Query("select b from BusinessManager b where b.businessMember.id = :business_id")
    List<BusinessManager> findAllBusinessManagersByBusinessMemberId(Long business_id);


}
