package digital.patron.PatronMembers.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import digital.patron.PatronMembers.domain.SaleMember;

import java.util.List;

import static digital.patron.PatronMembers.domain.QSaleMember.saleMember;

public class SaleMemberRepositoryImpl implements SaleMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public SaleMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<SaleMember> findSaleMembersByKeyword(String keyword) {
        return queryFactory
                .select(saleMember)
                .from(saleMember)
                .where(saleMember.email.eq(keyword)
                        .or(saleMember.name.eq(keyword))
                        .or(saleMember.accountInfo.public_wallet.eq(keyword))
                        .or(saleMember.status.eq(keyword)))
                .fetch();
    }
}
