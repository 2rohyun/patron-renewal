package digital.patron.PatronMembers.repository.custom;

import digital.patron.PatronMembers.domain.BusinessMember;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.SaleMember;

import java.util.List;

public interface BusinessMemberRepositoryCustom {
    List<BusinessMember> findBusinessMembersByKeyword(String keyword);
}
