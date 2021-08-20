package digital.patron.PatronMembers.repository;

import digital.patron.PatronMembers.domain.LeftMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeftMemberRepository extends JpaRepository<LeftMember,Long> {
}
