package digital.patron.PatronMembers.repository.MontlySubscritpion;

import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MonthlySubscriptionRepository extends JpaRepository<MonthlySubscription, Long> {
    @Query("select m from MonthlySubscription m where m.generalMember.id = :general_id")
    List<MonthlySubscription> findAllMonthlySubscriptionByGeneralMemberId(Long general_id);

    @Query("select m from MonthlySubscription m where m.saleMember.id = :sale_id")
    List<MonthlySubscription> findAllMonthlySubscriptionBySaleMemberId(Long sale_id);

    @Query("select m from MonthlySubscription m where m.businessMember.id = :business_id")
    List<MonthlySubscription> findAllMonthlySubscriptionByBusinessMemberId(Long business_id);

}
