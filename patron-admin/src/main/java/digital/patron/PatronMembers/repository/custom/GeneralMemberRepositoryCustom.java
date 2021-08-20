package digital.patron.PatronMembers.repository.custom;

import digital.patron.PatronMembers.domain.GeneralMember;

import java.util.List;

public interface GeneralMemberRepositoryCustom {

    List<GeneralMember> findGeneralMembersByKeyword(String keyword);

}
