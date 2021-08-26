package digital.patron.PatronMembers.repository;

import digital.patron.PatronMembers.domain.LeftMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LeftMemberRepository extends JpaRepository<LeftMember, Long> {
}
