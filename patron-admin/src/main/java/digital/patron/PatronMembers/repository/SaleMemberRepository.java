package digital.patron.PatronMembers.repository;

import digital.patron.PatronMembers.domain.SaleMember;
import digital.patron.PatronMembers.repository.custom.SaleMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SaleMemberRepository extends JpaRepository<SaleMember, Long>, SaleMemberRepositoryCustom {

    List<SaleMember> findAll();

    Optional<SaleMember> findSaleMemberById(Long sale_id);
}
