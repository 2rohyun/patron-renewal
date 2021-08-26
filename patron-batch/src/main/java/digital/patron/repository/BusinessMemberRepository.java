package digital.patron.repository;

import digital.patron.domain.BusinessMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {
}
