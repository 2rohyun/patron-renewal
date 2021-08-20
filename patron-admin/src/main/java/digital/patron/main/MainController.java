package digital.patron.main;

import digital.patron.PatronMembers.domain.BusinessInformation.BusinessManager;
import digital.patron.PatronMembers.domain.BusinessMember;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscription;
import digital.patron.PatronMembers.domain.SaleMember;
import digital.patron.PatronMembers.dto.SearchMemberDto;
import digital.patron.PatronMembers.service.PatronMembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
//@RequestMapping("/admin")
@RequiredArgsConstructor
public class MainController {

    private final PatronMembersService patronMembersService;

    //General Member
    @GetMapping("/general-user")
    public String generalUser(Model model){

        List<GeneralMember> generalMember = patronMembersService.getAllGeneralMembers();

        model.addAttribute("generalMember", generalMember);
        return "general-member";
    }
    @GetMapping("api/general-user-search")
    @ResponseBody
    public ResponseEntity<?> generalUserSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<GeneralMember> generalMembers = patronMembersService.searchGeneralMemberByKeyword(keyword);

        SearchMemberDto searchMemberDto = new SearchMemberDto(
                generalMembers.stream().map(g->g.getId()).collect(Collectors.toList()),
                generalMembers.stream().map(g->g.getEmail()).collect(Collectors.toList()),
                generalMembers.stream().map(g->g.getName()).collect(Collectors.toList()),
                generalMembers.stream().map(g->g.getStatus()).collect(Collectors.toList()),
                generalMembers.stream().map(g->g.getCreate_time()).collect(Collectors.toList()),
                generalMembers.stream().map(g->g.getPublic_wallet()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemberDto);
        }
    @GetMapping("/general-user-detail")
    public String generalUserDetail(@RequestParam Long general_id, Model model){
        GeneralMember generalMemberDetails = patronMembersService.findGeneralMemberById(general_id);
        List<MonthlySubscription> generalMemberMonthlySubscriptions = patronMembersService.getMonthlySubscriptionByGeneralMemberId(general_id);
        model.addAttribute("generalMemberDetails", generalMemberDetails);
        model.addAttribute("generalMemberMonthlySubscriptions", generalMemberMonthlySubscriptions);

        return "general-user-detail";
    }
    //Sale Member
    @GetMapping("/sales-user")
    public String saleUser(Model model){

        List<SaleMember> saleMembers = patronMembersService.getAllSaleMembers();

        model.addAttribute("saleMembers", saleMembers);
        return "sales-member";
    }
    @GetMapping("api/sales-user-search")
    @ResponseBody
    public ResponseEntity<?> saleUserSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<SaleMember> saleMembers = patronMembersService.searchSaleMemberByKeyword(keyword);

        SearchMemberDto searchMemberDto = new SearchMemberDto(
                saleMembers.stream().map(g->g.getId()).collect(Collectors.toList()),
                saleMembers.stream().map(g->g.getEmail()).collect(Collectors.toList()),
                saleMembers.stream().map(g->g.getName()).collect(Collectors.toList()),
                saleMembers.stream().map(g->g.getStatus()).collect(Collectors.toList()),
                saleMembers.stream().map(g->g.getCreate_time()).collect(Collectors.toList()),
                saleMembers.stream().map(g->g.getAccountInformation().getPublic_wallet()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemberDto);
    }
    @GetMapping("/sales-user-detail")
    public String saleUserDetail(@RequestParam Long sale_id, Model model){
        SaleMember saleMemberDetails = patronMembersService.findSaleMemberById(sale_id);
        List<MonthlySubscription> saleMemberMonthlySubscriptions = patronMembersService.getMonthlySubscriptionBySaleMemberId(sale_id);
        model.addAttribute("saleMemberDetails", saleMemberDetails);
        model.addAttribute("saleMemberMonthlySubscriptions", saleMemberMonthlySubscriptions);

        return "sales-user-detail";
    }

    //BusinessMember
    @GetMapping("/corporate-user")
    public String businessUser(Model model){

        List<BusinessMember> businessMembers = patronMembersService.getAllBusinessMembers();

        model.addAttribute("businessMember", businessMembers);
        return "corporate-member";
    }
    @GetMapping("api/corporate-user-search")
    @ResponseBody
    public ResponseEntity<?> businessUserSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<BusinessMember> businessMembers = patronMembersService.searchBusinessMemberByKeyword(keyword);

        SearchMemberDto searchMemberDto = new SearchMemberDto(
                businessMembers.stream().map(g->g.getId()).collect(Collectors.toList()),
                businessMembers.stream().map(g->g.getEmail()).collect(Collectors.toList()),
                businessMembers.stream().map(g->g.getName()).collect(Collectors.toList()),
                businessMembers.stream().map(g->g.getStatus()).collect(Collectors.toList()),
                businessMembers.stream().map(g->g.getCreate_time()).collect(Collectors.toList()),
                businessMembers.stream().map(g->g.getAccountInformation().getPublic_wallet()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemberDto);
    }
    @GetMapping("/corporate-user-detail")
    public String businessUserDetail(@RequestParam Long business_id, Model model){
        BusinessMember businessMemberDetails = patronMembersService.findBusinessMemberById(business_id);
        List<MonthlySubscription> businessMemberMonthlySubscriptions = patronMembersService.getMonthlySubscriptionByBusinessMemberId(business_id);
        List<BusinessManager> businessMemberManagers = patronMembersService.getBusinessManagersByBusinessMemberId(business_id);

        model.addAttribute("businessMemberDetails", businessMemberDetails);
        model.addAttribute("businessMemberMonthlySubscriptions", businessMemberMonthlySubscriptions);
        model.addAttribute("businessMemberManagers", businessMemberManagers);

        return "corporate-user-detail";
    }


}
