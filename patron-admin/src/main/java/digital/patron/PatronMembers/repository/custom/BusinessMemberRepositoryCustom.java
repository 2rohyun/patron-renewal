package digital.patron.PatronMembers.repository.custom;

import digital.patron.PatronMembers.domain.BusinessMember;

import java.util.List;

public interface BusinessMemberRepositoryCustom {
    List<BusinessMember> findBusinessMembersByKeyword(String keyword);
}
