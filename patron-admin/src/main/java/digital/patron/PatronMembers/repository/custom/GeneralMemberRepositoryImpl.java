package digital.patron.PatronMembers.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import digital.patron.PatronMembers.domain.GeneralMember;

import java.util.List;

import static digital.patron.PatronMembers.domain.QGeneralMember.generalMember;

public class GeneralMemberRepositoryImpl implements GeneralMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GeneralMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<GeneralMember> findGeneralMembersByKeyword(String keyword) {
        return queryFactory
                .select(generalMember)
                .from(generalMember)
                .where(generalMember.email.eq(keyword)
                        .or(generalMember.name.eq(keyword))
                        .or(generalMember.public_wallet.eq(keyword))
                        .or(generalMember.status.eq(keyword)))
                .fetch();
    }
}
