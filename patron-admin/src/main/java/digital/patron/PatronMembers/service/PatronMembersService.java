package digital.patron.PatronMembers.service;

import digital.patron.PatronMembers.domain.BusinessInformation.BusinessManager;
import digital.patron.PatronMembers.domain.BusinessMember;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscription;
import digital.patron.PatronMembers.domain.SaleMember;
import digital.patron.PatronMembers.repository.BusinessInformation.BusinessManagerRepository;
import digital.patron.PatronMembers.repository.BusinessMemberRepository;
import digital.patron.PatronMembers.repository.GeneralMemberRepository;
import digital.patron.PatronMembers.repository.MontlySubscritpion.MonthlySubscriptionRepository;
import digital.patron.PatronMembers.repository.SaleMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PatronMembersService {
    private final GeneralMemberRepository generalMemberRepository;
    private final SaleMemberRepository saleMemberRepository;
    private final BusinessMemberRepository businessMemberRepository;
    private final MonthlySubscriptionRepository monthlySubscriptionRepository;
    private final BusinessManagerRepository businessManagerRepository;


    //General Member Functions
    public List<GeneralMember> getAllGeneralMembers(){
        return generalMemberRepository.findAll();
    }

    public List<GeneralMember> searchGeneralMemberByKeyword(String keyword){
        return generalMemberRepository.findGeneralMembersByKeyword(keyword);
    }

    public List<MonthlySubscription> getMonthlySubscriptionByGeneralMemberId(Long general_id){
        return monthlySubscriptionRepository.findAllMonthlySubscriptionByGeneralMemberId(general_id);
    }

    public GeneralMember findGeneralMemberById(Long general_id){
        return generalMemberRepository.findGeneralMemberById(general_id).orElseThrow( ()->
                new IllegalArgumentException("No General member with such Id"));
    }


    //SaleMemberFunctions
    public List<SaleMember> getAllSaleMembers(){
        return saleMemberRepository.findAll();
    }

    public List<SaleMember> searchSaleMemberByKeyword(String keyword){
        return saleMemberRepository.findSaleMembersByKeyword(keyword);
    }

    public List<MonthlySubscription> getMonthlySubscriptionBySaleMemberId(Long sale_id){
        return monthlySubscriptionRepository.findAllMonthlySubscriptionBySaleMemberId(sale_id);
    }

    public SaleMember findSaleMemberById(Long sale_id){
        return saleMemberRepository.findSaleMemberById(sale_id).orElseThrow( ()->
                new IllegalArgumentException("No Sale member with such Id"));
    }

    //BusinessMemberFunctions
    public List<BusinessMember> getAllBusinessMembers(){
        return businessMemberRepository.findAll();
    }

    public List<BusinessMember> searchBusinessMemberByKeyword(String keyword){
        return businessMemberRepository.findBusinessMembersByKeyword(keyword);
    }

    public List<MonthlySubscription> getMonthlySubscriptionByBusinessMemberId(Long business_id){
        return monthlySubscriptionRepository.findAllMonthlySubscriptionByBusinessMemberId(business_id);
    }
    public List<BusinessManager> getBusinessManagersByBusinessMemberId(Long business_id){
        return businessManagerRepository.findAllBusinessManagersByBusinessMemberId(business_id);
    }

    public BusinessMember findBusinessMemberById(Long business_id){
        return businessMemberRepository.findBusinessMemberById(business_id).orElseThrow( ()->
                new IllegalArgumentException("No Business member with such Id"));
    }

}
