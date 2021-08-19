package digital.patron.repository;

import digital.patron.domain.StreamingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamingStatisticsRepository extends JpaRepository<StreamingStatistics, Long> {
}
