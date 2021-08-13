package digital.patron.repository;

import digital.patron.domain.SaleMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleMemberRepository extends JpaRepository<SaleMember, Long> {
}
