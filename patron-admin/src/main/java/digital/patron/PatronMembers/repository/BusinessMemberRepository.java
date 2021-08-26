package digital.patron.PatronMembers.repository;

import digital.patron.PatronMembers.domain.BusinessMember;
import digital.patron.PatronMembers.repository.custom.BusinessMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long>, BusinessMemberRepositoryCustom {

    List<BusinessMember> findAll();

    Optional<BusinessMember> findBusinessMemberById(Long business_id);
}
