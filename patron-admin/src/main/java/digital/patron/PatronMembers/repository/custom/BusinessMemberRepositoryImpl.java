package digital.patron.PatronMembers.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import digital.patron.PatronMembers.domain.BusinessMember;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.QBusinessMember;
import digital.patron.PatronMembers.domain.QGeneralMember;

import java.util.List;

public class BusinessMemberRepositoryImpl implements BusinessMemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public BusinessMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<BusinessMember> findBusinessMembersByKeyword(String keyword) {
        return queryFactory
                .select(QBusinessMember.businessMember)
                .from(QBusinessMember.businessMember)
                .where(QBusinessMember.businessMember.email.eq(keyword)
                        .or(QBusinessMember.businessMember.name.eq(keyword))
                        .or(QBusinessMember.businessMember.accountInformation.public_wallet.eq(keyword))
                        .or(QBusinessMember.businessMember.status.eq(keyword)))
                .fetch();
    }
}
