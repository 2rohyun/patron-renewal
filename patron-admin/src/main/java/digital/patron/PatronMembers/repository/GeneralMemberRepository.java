package digital.patron.PatronMembers.repository;

import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.repository.custom.GeneralMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GeneralMemberRepository extends JpaRepository<GeneralMember,Long>, GeneralMemberRepositoryCustom {

    List<GeneralMember> findAll();

    Optional<GeneralMember> findGeneralMemberById(Long generalId);
}
