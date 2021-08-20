package digital.patron.PatronMembers.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.QGeneralMember;
import digital.patron.PatronMembers.domain.QSaleMember;
import digital.patron.PatronMembers.domain.SaleMember;

import java.util.List;

public class SaleMemberRepositoryImpl implements SaleMemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public SaleMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<SaleMember> findSaleMembersByKeyword(String keyword) {
        return queryFactory
                .select(QSaleMember.saleMember)
                .from(QSaleMember.saleMember)
                .where(QSaleMember.saleMember.email.eq(keyword)
                        .or(QSaleMember.saleMember.name.eq(keyword))
                        .or(QSaleMember.saleMember.accountInformation.public_wallet.eq(keyword))
                        .or(QSaleMember.saleMember.status.eq(keyword)))
                .fetch();
    }
}
