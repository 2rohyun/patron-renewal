package digital.patron.repository;

import digital.patron.domain.GeneralMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralMemberRepository extends JpaRepository<GeneralMember, Long> {
}
