package digital.patron.repository;

import digital.patron.domain.MonthSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MonthSubscriptionRepository extends JpaRepository<MonthSubscription, Long> {

    @Query("select sum(m)" +
            " from MonthSubscription m" +
            " where m.membershipStartTime >= :startDateTime" +
            " and m.membershipStartTime <= :endDateTime")
    Integer findAllMonthSubscriptionByStartDateAndEndDate(@Param("startDateTime") LocalDateTime startDateTime,
                                                          @Param("endDateTime") LocalDateTime endDateTime);
}
