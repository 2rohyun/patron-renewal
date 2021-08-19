package digital.patron.repository;

import digital.patron.domain.GeneralMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralMemberRepository extends JpaRepository<GeneralMember, Long> {
}
