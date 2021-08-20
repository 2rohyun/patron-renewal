package digital.patron.repository;

import digital.patron.domain.StreamingStatistics;
import digital.patron.domain.StreamingStatisticsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreamingStatisticsDetailRepository extends JpaRepository<StreamingStatisticsDetail, Long> {
}
