package digital.patron.tasklet;

import digital.patron.domain.*;
import digital.patron.repository.BusinessMemberRepository;
import digital.patron.repository.GeneralMemberRepository;
import digital.patron.repository.SaleMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class SaveMemberTasklet implements Tasklet {

    private final int SIZE = 3_000;
    private final GeneralMemberRepository generalMemberRepository;
    private final SaleMemberRepository saleMemberRepository;
    private final BusinessMemberRepository businessMemberRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<GeneralMember> generalMembers = createGeneralMembers();
        List<SaleMember> saleMembers = createSaleMembers();
        List<BusinessMember> businessMembers = createBusinessMembers();

        Collections.shuffle(generalMembers);
        Collections.shuffle(saleMembers);
        Collections.shuffle(businessMembers);

        generalMemberRepository.saveAll(generalMembers);
        saleMemberRepository.saveAll(saleMembers);
        businessMemberRepository.saveAll(businessMembers);

        return RepeatStatus.FINISHED;
    }

    private List<GeneralMember> createGeneralMembers() {
        List<GeneralMember> generalMembers = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            String gender;
            int month;
            if (i % 2 == 0) {
                gender = "male";
                month = 8;
            }
            else {
                gender = "female";
                month = 9;
            }
            generalMembers.add(new GeneralMember(
                    null,
                    "general_member_email" + i,
                    "1234",
                    "general_member_name" + i,
                    "REGISTERED",
                    LocalDate.now(),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Korea",
                    gender,
                    new MonthSubscription(
                            null,
                            "BASIC",
                            LocalDateTime.of(2021,month,1,13,30),
                            LocalDateTime.of(2021,month,1,13,30).plusDays(30),
                            new BigDecimal("8000"),
                            "pg_agency",
                            "pg_method",
                            123456,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            new MonthSubscriptionSales(null, new BigDecimal("800"), new BigDecimal("7200"))
                    ))
            );
        }

        return generalMembers;
    }

    private List<SaleMember> createSaleMembers() {
        List<SaleMember> saleMembers = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            String gender;
            int month;
            BigDecimal ratio;
            boolean chargeFree;
            if (i % 2 == 0) {
                gender = "male";
                month = 8;
                ratio = new BigDecimal("0.5");
                chargeFree = false;
            }
            else {
                gender = "female";
                month = 9;
                ratio = new BigDecimal("0.7");
                chargeFree = true;
            }

            saleMembers.add(new SaleMember(
                    null,
                    "sale_member_email" + i,
                    "1234",
                    "sale_member_name" + i,
                    "REGISTERED",
                    LocalDate.now(),
                    LocalDate.now(),
                    "Korea",
                    gender,
                    ratio,
                    new MonthSubscription(
                            null,
                            "BASIC",
                            LocalDateTime.of(2021, month, 1, 13, 30),
                            LocalDateTime.of(2021, month, 1, 13, 30).plusDays(30),
                            new BigDecimal("8000"),
                            "pg_agency",
                            "pg_method",
                            123456,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            new MonthSubscriptionSales(null, new BigDecimal("800"), new BigDecimal("7200"))),
                    new AccountInfo(null, "public_wallet", "bank", "account_number", "bankbook"),
                    Collections.singletonList(new Artwork(
                            null,
                            "code" + i,
                            "name" + i,
                            "intro" + i,
                            "size" + i,
                            "year" + i,
                            "keep" + i,
                            true,
                            true,
                            chargeFree,
                            i,
                            i * 2,
                            i * 2 - i,
                            LocalDate.now(),
                            LocalDate.now(),
                            LocalDateTime.now(),
                            null,
                            null
                    ))
                    )
            );
        }

        return saleMembers;
    }

    private List<BusinessMember> createBusinessMembers() {
        List<BusinessMember> businessMembers = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            String gender;
            int month;
            BigDecimal ratio;
            boolean chargeFree;
            if (i % 2 == 0) {
                gender = "male";
                month = 8;
                ratio = new BigDecimal("0.5");
                chargeFree = false;
            }
            else {
                gender = "female";
                month = 9;
                ratio = new BigDecimal("0.7");
                chargeFree = true;
            }

            businessMembers.add(new BusinessMember(
                            null,
                            "business_member_email" + i,
                            "1234",
                            "business_member_name" + i,
                            "REGISTERED",
                            LocalDate.now(),
                            LocalDate.now(),
                            "Korea",
                            gender,
                            ratio,
                            new MonthSubscription(
                                    null,
                                    "PREMIUM",
                                    LocalDateTime.of(2021, month, 1, 13, 30),
                                    LocalDateTime.of(2021, month, 1, 13, 30).plusDays(30),
                                    new BigDecimal("12000"),
                                    "pg_agency",
                                    "pg_method",
                                    123456,
                                    LocalDateTime.now(),
                                    LocalDateTime.now(),
                                    new MonthSubscriptionSales(null, new BigDecimal("1200"), new BigDecimal("10800"))),
                            new AccountInfo(null, "public_wallet", "bank", "account_number", "bankbook"),
                            Collections.singletonList(new Artwork(
                                    null,
                                    "code" + (i + 3000),
                                    "name" + (i + 3000),
                                    "intro" + (i + 3000),
                                    "size" + (i + 3000),
                                    "year" + (i + 3000),
                                    "keep" + (i + 3000),
                                    true,
                                    true,
                                    chargeFree,
                                    i,
                                    i * 2,
                                    i * 2 - i,
                                    LocalDate.now(),
                                    LocalDate.now(),
                                    LocalDateTime.now(),
                                    null,
                                    null
                            ))
                    )
            );
        }
        return businessMembers;
    }
}
