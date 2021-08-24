package digital.patron.repository;

import digital.patron.domain.SaleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleMemberRepository extends JpaRepository<SaleMember, Long> {

    @Query("select min(s.id) from SaleMember s")
    long findMinId();

    @Query("select max(s.id) from SaleMember s")
    long findMaxId();
}
