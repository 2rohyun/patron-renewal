package digital.patron.repository;

import digital.patron.domain.BusinessMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {
}
