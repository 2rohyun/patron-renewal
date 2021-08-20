package digital.patron;

import digital.patron.AdminMembers.domain.Admin;
import digital.patron.AdminMembers.repository.AdminRepository;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.MonthlySubscription.AccountInformation;
import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscription;
import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscriptionSales;
import digital.patron.PatronMembers.domain.SaleMember;
import digital.patron.PatronMembers.repository.AccountInformation.AccountInformationRepository;
import digital.patron.PatronMembers.repository.BusinessInformation.BusinessLicenseRepository;
import digital.patron.PatronMembers.repository.BusinessInformation.BusinessManagerRepository;
import digital.patron.PatronMembers.repository.BusinessMemberRepository;
import digital.patron.PatronMembers.repository.GeneralMemberRepository;
import digital.patron.PatronMembers.repository.LeftMemberRepository;
import digital.patron.PatronMembers.repository.MontlySubscritpion.MonthlySubscriptionRepository;
import digital.patron.PatronMembers.repository.MontlySubscritpion.MonthlySubscriptionSalesRepository;
import digital.patron.PatronMembers.repository.SaleMemberRepository;
import digital.patron.common.domain.Role;
import digital.patron.common.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.InitAdmin();
        initService.InitPatronMembers();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {


        private final AdminRepository adminRepository;
//        private final AccountInformationRepository accountInformationRepository;
//        private final BusinessLicenseRepository businessLicenseRepository;
//        private final BusinessManagerRepository businessManagerRepository;
        private final MonthlySubscriptionRepository monthlySubscriptionRepository;
        private final MonthlySubscriptionSalesRepository monthlySubscriptionSalesRepository;
//        private final BusinessMemberRepository businessMemberRepository;
        private final GeneralMemberRepository generalMemberRepository;
//        private final LeftMemberRepository leftMemberRepository;
//        private final SaleMemberRepository saleMemberRepository;


        @Transactional
        public void InitAdmin() {

            Admin admin1 = new Admin("admin@gmail.com", "01011112222", LocalDateTime.of(2020, 2, 1, 12, 31), "$2a$12$owLor5bVSL0nnEnxURV32uvHw18kpbJ5h1YNxO7x3elAX34CEi5IC", Status.ACTIVE, Role.ADMIN);
            Admin admin2 = new Admin("second_admin@gmail.com", "01012345678",LocalDateTime.of(2020, 2, 1, 12, 31), "$2a$12$DDDA.dXE3.sOHgsVXswYqe3jbSJR23pqCYGK65gZRCJMmNfc7EoyS", Status.BANNED, Role.USER);

            adminRepository.save(admin1);
            adminRepository.save(admin2);
        }
        @Transactional
        public void InitPatronMembers() {

            GeneralMember generalMember1 =new GeneralMember("general_member1@gmail.com","generalMember","status1",LocalDateTime.of(2020, 2, 1, 12, 31),LocalDateTime.now(),"publicWallet",LocalDateTime.of(2020, 2, 1, 12, 31),"korean","male","password1");
            GeneralMember generalMember2 =new GeneralMember("general_member2@gmail.com","generalMember2","status2",LocalDateTime.of(2020, 2, 1, 12, 31),LocalDateTime.now(),"publicWallet2",LocalDateTime.of(2020, 2, 1, 12, 31),"american","female","password2");

            MonthlySubscription monthlySubscription1 = new MonthlySubscription("membership1","membershipPeriod1","amount1","pg_agency1","pg_method1",1,LocalDateTime.now(),LocalDateTime.now());
            MonthlySubscription monthlySubscription2 = new MonthlySubscription("membership2","membershipPeriod2","amount2","pg_agency2","pg_method2",2,LocalDateTime.now(),LocalDateTime.now());

            MonthlySubscriptionSales monthlySubscriptionSales1 = new MonthlySubscriptionSales("fee1","profit1");
            MonthlySubscriptionSales monthlySubscriptionSales2 = new MonthlySubscriptionSales("fee2","profit2");
            monthlySubscriptionSalesRepository.save(monthlySubscriptionSales1);
            monthlySubscriptionSalesRepository.save(monthlySubscriptionSales2);


            monthlySubscription1.setMonthlySubscriptionSales(monthlySubscriptionSales2);
            monthlySubscription2.setMonthlySubscriptionSales(monthlySubscriptionSales1);

            monthlySubscriptionRepository.save(monthlySubscription1);
            monthlySubscriptionRepository.save(monthlySubscription2);

            generalMember1.addMonthlySubscription(monthlySubscription1);
            generalMember1.addMonthlySubscription(monthlySubscription2);
            generalMember2.addMonthlySubscription(monthlySubscription2);

            generalMemberRepository.save(generalMember1);
            generalMemberRepository.save(generalMember2);


//            SaleMember saleMember1 = new SaleMember("sale_member1@gmail.com","saleMember1","status1",LocalDateTime.of(2020, 2, 1, 12, 31),LocalDateTime.of(2020, 2, 1, 12, 31),"korean","male","password1");
//            SaleMember saleMember2 = new SaleMember("sale_member2@gmail.com","saleMember2","status2",LocalDateTime.of(2020, 2, 1, 12, 31),LocalDateTime.of(2020, 2, 1, 12, 31),"american","female","password2");
//
//            AccountInformation accountInformation1 = new AccountInformation("publicWallet1","bankName1","bankNumber1","bankBook1");
//            AccountInformation accountInformation2 = new AccountInformation("publicWallet2","bankName2","bankNumber2","bankBook2");
//
//            saleMember1.setAccountInformation(accountInformation2);
//            saleMember2.setAccountInformation(accountInformation1);



        }
    }
}