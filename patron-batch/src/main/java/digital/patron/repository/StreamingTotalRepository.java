package digital.patron.repository;

import digital.patron.domain.StreamingTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface StreamingTotalRepository extends JpaRepository<StreamingTotal, Long> {

    Optional<StreamingTotal> findByAggregationStartTime(LocalDateTime date);
}
