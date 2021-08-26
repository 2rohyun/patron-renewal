package digital.patron.repository;

import digital.patron.domain.StreamingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StreamingStatisticsRepository extends JpaRepository<StreamingStatistics, Long> {
    Optional<StreamingStatistics> findByOwnerEmail(String email);
    List<StreamingStatistics> findByUpdateTimeBetween(LocalDateTime start, LocalDateTime end);
}
