package digital.patron.PatronMembers.repository.custom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.QGeneralMember;


import java.util.List;


public class GeneralMemberRepositoryImpl implements GeneralMemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public GeneralMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<GeneralMember> findGeneralMembersByKeyword(String keyword) {
        return queryFactory
                .select(QGeneralMember.generalMember)
                .from(QGeneralMember.generalMember)
                .where(QGeneralMember.generalMember.email.eq(keyword)
                        .or(QGeneralMember.generalMember.name.eq(keyword))
                        .or(QGeneralMember.generalMember.public_wallet.eq(keyword))
                        .or(QGeneralMember.generalMember.status.eq(keyword)))
                .fetch();
    }
}
