package digital.patron.service;

import digital.patron.repository.GeneralMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GeneralMemberAdminService {

    private final GeneralMemberRepository generalMemberRepository;

    public GeneralMemberAdminService(GeneralMemberRepository generalMemberRepository) {
        this.generalMemberRepository = generalMemberRepository;
    }
}
