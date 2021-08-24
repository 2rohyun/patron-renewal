package digital.patron.repository;

import digital.patron.domain.BusinessMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {

    @Query("select min(b.id) from BusinessMember b")
    long findMinId();

    @Query("select max(b.id) from BusinessMember b")
    long findMaxId();
}
