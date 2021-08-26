package digital.patron.AdminMembers.service;


import digital.patron.AdminMembers.domain.Admin;
import digital.patron.AdminMembers.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminMembersService {

    private final AdminRepository adminRepository;

    public void setNewPasswordForAdmin(String email,String password){
        adminRepository.setNewPasswordForAdminByEmail(email,password);
    }

    public List<Admin> getAdmins(){
        return adminRepository.findAll();
    }
}
