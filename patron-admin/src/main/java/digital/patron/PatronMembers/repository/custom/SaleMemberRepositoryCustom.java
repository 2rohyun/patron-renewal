package digital.patron.PatronMembers.repository.custom;

import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.SaleMember;

import java.util.List;

public interface SaleMemberRepositoryCustom {
    List<SaleMember> findSaleMembersByKeyword(String keyword);
}
