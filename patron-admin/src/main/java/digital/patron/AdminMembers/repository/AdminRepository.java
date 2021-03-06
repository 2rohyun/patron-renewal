package digital.patron.AdminMembers.repository;

import digital.patron.AdminMembers.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String deviceId);

    @Query("update Admin a set a.password = :password where a.email = :email")
    void setNewPasswordForAdminByEmail(String email,String password);
}
