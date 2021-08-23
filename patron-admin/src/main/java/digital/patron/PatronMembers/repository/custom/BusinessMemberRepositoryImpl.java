package digital.patron.PatronMembers.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import digital.patron.PatronMembers.domain.BusinessMember;

import java.util.List;

import static digital.patron.PatronMembers.domain.QBusinessMember.businessMember;

public class BusinessMemberRepositoryImpl implements BusinessMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BusinessMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<BusinessMember> findBusinessMembersByKeyword(String keyword) {
        return queryFactory
                .select(businessMember)
                .from(businessMember)
                .where(businessMember.email.eq(keyword)
                        .or(businessMember.name.eq(keyword))
                        .or(businessMember.accountInfo.public_wallet.eq(keyword))
                        .or(businessMember.status.eq(keyword)))
                .fetch();
    }
}
