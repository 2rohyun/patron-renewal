package digital.patron.main;

import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.AdminMembers.domain.Admin;
import digital.patron.AdminMembers.repository.AdminRepository;
import digital.patron.AdminMembers.service.AdminMembersService;
import digital.patron.ContentsManagement.domain.artist.DeathArtist;
import digital.patron.ContentsManagement.domain.artist.SurviveArtist;
import digital.patron.ContentsManagement.domain.artwork.Artwork;
import digital.patron.ContentsManagement.domain.exhibition.Exhibition;
import digital.patron.ContentsManagement.dto.ApprovedArtworkDto;
import digital.patron.ContentsManagement.dto.SearchNoApprovalArtworkDto;
import digital.patron.ContentsManagement.service.ContentsManagementService;
import digital.patron.PatronMembers.domain.BusinessInformation.BusinessManager;
import digital.patron.PatronMembers.domain.BusinessMember;
import digital.patron.PatronMembers.domain.GeneralMember;
import digital.patron.PatronMembers.domain.LeftMember;
import digital.patron.PatronMembers.domain.MonthlySubscription.MonthlySubscription;
import digital.patron.PatronMembers.domain.SaleMember;
import digital.patron.PatronMembers.dto.BusinessManagerDto;
import digital.patron.PatronMembers.dto.SearchMemberDto;
import digital.patron.PatronMembers.repository.BusinessInformation.BusinessManagerRepository;
import digital.patron.PatronMembers.service.PatronMembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MainController {

    private final PatronMembersService patronMembersService;
    private final ContentsManagementService contentsManagementService;
    private final AdminMembersService adminMembersService;

    //General Member
    @GetMapping("/general-member")
    public String generalMember(Model model) {
        List<GeneralMember> generalMember = patronMembersService.getAllGeneralMembers();

        model.addAttribute("generalMember", generalMember);
        return "general-member";
    }


    @GetMapping("api/general-member-search")
    @ResponseBody
    public ResponseEntity<?> generalMemberSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<GeneralMember> generalMembers = patronMembersService.searchGeneralMemberByKeyword(keyword);

        SearchMemberDto searchMemberDto = new SearchMemberDto(
                generalMembers.stream().map(g -> g.getId()).collect(Collectors.toList()),
                generalMembers.stream().map(g -> g.getEmail()).collect(Collectors.toList()),
                generalMembers.stream().map(g -> g.getName()).collect(Collectors.toList()),
                generalMembers.stream().map(g -> g.getStatus()).collect(Collectors.toList()),
                generalMembers.stream().map(g -> g.getCreate_time()).collect(Collectors.toList()),
                generalMembers.stream().map(g -> g.getPublic_wallet()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemberDto);
    }

    @GetMapping("/general-member-detail")
    public String generalMemberDetail(@RequestParam Long general_id, Model model) {
        GeneralMember generalMemberDetails = patronMembersService.findGeneralMemberById(general_id);
        List<MonthlySubscription> generalMemberMonthlySubscriptions = patronMembersService.getMonthlySubscriptionByGeneralMemberId(general_id);
        model.addAttribute("generalMemberDetails", generalMemberDetails);
        model.addAttribute("generalMemberMonthlySubscriptions", generalMemberMonthlySubscriptions);

        return "general-member-detail";
    }

    //Sale Member
    @GetMapping("/sales-member")
    public String saleMember(Model model) {

        List<SaleMember> saleMembers = patronMembersService.getAllSaleMembers();

        model.addAttribute("saleMembers", saleMembers);
        return "sales-member";
    }

    @GetMapping("api/sales-member-search")
    @ResponseBody
    public ResponseEntity<?> saleMemberSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<SaleMember> saleMembers = patronMembersService.searchSaleMemberByKeyword(keyword);

        SearchMemberDto searchMemberDto = new SearchMemberDto(
                saleMembers.stream().map(g -> g.getId()).collect(Collectors.toList()),
                saleMembers.stream().map(g -> g.getEmail()).collect(Collectors.toList()),
                saleMembers.stream().map(g -> g.getName()).collect(Collectors.toList()),
                saleMembers.stream().map(g -> g.getStatus()).collect(Collectors.toList()),
                saleMembers.stream().map(g -> g.getCreate_time()).collect(Collectors.toList()),
                saleMembers.stream().map(g -> g.getAccountInfo().getPublic_wallet()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemberDto);
    }

    @GetMapping("/sales-member-detail")
    public String saleMemberDetail(@RequestParam Long sale_id, Model model) {
        SaleMember saleMemberDetails = patronMembersService.findSaleMemberById(sale_id);
        List<MonthlySubscription> saleMemberMonthlySubscriptions = patronMembersService.getMonthlySubscriptionBySaleMemberId(sale_id);
        model.addAttribute("saleMemberDetails", saleMemberDetails);
        model.addAttribute("saleMemberMonthlySubscriptions", saleMemberMonthlySubscriptions);
        return "sales-member-detail";
    }

    //BusinessMember
    @GetMapping("/corporate-member")
    public String businessMember(Model model) {
        List<BusinessMember> businessMembers = patronMembersService.getAllBusinessMembers();

        model.addAttribute("businessMember", businessMembers);
        return "corporate-member";
    }

    @GetMapping("api/corporate-member-search")
    @ResponseBody
    public ResponseEntity<?> businessMemberSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<BusinessMember> businessMembers = patronMembersService.searchBusinessMemberByKeyword(keyword);

        SearchMemberDto searchMemberDto = new SearchMemberDto(
                businessMembers.stream().map(g -> g.getId()).collect(Collectors.toList()),
                businessMembers.stream().map(g -> g.getEmail()).collect(Collectors.toList()),
                businessMembers.stream().map(g -> g.getName()).collect(Collectors.toList()),
                businessMembers.stream().map(g -> g.getStatus()).collect(Collectors.toList()),
                businessMembers.stream().map(g -> g.getCreate_time()).collect(Collectors.toList()),
                businessMembers.stream().map(g -> g.getAccountInfo().getPublic_wallet()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchMemberDto);
    }

    @GetMapping("/corporate-member-detail")
    public String businessMemberDetail(@RequestParam Long business_id, Model model) {
        BusinessMember businessMemberDetails = patronMembersService.findBusinessMemberById(business_id);
        List<MonthlySubscription> businessMemberMonthlySubscriptions = patronMembersService.getMonthlySubscriptionByBusinessMemberId(business_id);
        List<BusinessManager> businessMemberManagers = patronMembersService.getBusinessManagersByBusinessMemberId(business_id);

        model.addAttribute("businessMemberDetails", businessMemberDetails);
        model.addAttribute("businessMemberMonthlySubscriptions", businessMemberMonthlySubscriptions);
        model.addAttribute("businessMemberManagers", businessMemberManagers);

        return "corporate-member-detail";
    }

    @PostMapping("/new-corporate-manager")
    public void newBusinessManager(@RequestBody BusinessManagerDto businessManagerDto) {

        BusinessManager businessManager = new BusinessManager(
                businessManagerDto.getEmail(),
                businessManagerDto.getName(),
                businessManagerDto.getPermission(),
                LocalDateTime.now(),
                null,
                businessManagerDto.getPhone_number()
        );
        patronMembersService.saveBusinessManager(businessManager);
    }
    //LeftMember
    @GetMapping("/left-member")
    public String leftMember(Model model) {

        List<LeftMember> leftMembers = patronMembersService.getAllLeftMembers();

        model.addAttribute("leftMembers", leftMembers);
        return "left-member";
    }

    //WaitingApproval
    @GetMapping("/waiting-approval")
    public String waitingApproval(Model model){
        List<Artwork> noApprovalArtworks = contentsManagementService.getNoApprovalArtworks();

        model.addAttribute("noApprovalArtworks",noApprovalArtworks);
        return "waiting-approval";
    }

    @GetMapping("api/waiting-approval-search")
    @ResponseBody
    public ResponseEntity<?> waitingApprovalSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<Artwork> noApprovalArtworksSearchResult = contentsManagementService.searchNoApprovalArtworkByKeyword(keyword);

        SearchNoApprovalArtworkDto searchNoApprovalArtworkDto = new SearchNoApprovalArtworkDto(
                noApprovalArtworksSearchResult.stream().map(a -> a.getId()).collect(Collectors.toList()),
                noApprovalArtworksSearchResult.stream().map(a -> a.getBusinessMember().getEmail()).collect(Collectors.toList()),
                noApprovalArtworksSearchResult.stream().map(a -> a.getArtworkName()).collect(Collectors.toList()),
                noApprovalArtworksSearchResult.stream().map(a -> a.getContentsThumbnail().getDefaultImg()).collect(Collectors.toList()),
                noApprovalArtworksSearchResult.stream().map(a -> a.getDeathArtist() != null ? a.getDeathArtist().getKorName() : a.getSurviveArtist().getKorName()).collect(Collectors.toList()),
                noApprovalArtworksSearchResult.stream().map(a -> a.getRegisteredAt()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(searchNoApprovalArtworkDto);
    }

    @GetMapping("/waiting-approval-detail")
    public String waitingApprovalDetail(@RequestParam Long artwork_id, Model model) {
        Artwork noApprovalArtwork = contentsManagementService.findArtworkById(artwork_id);

        model.addAttribute("noApprovalArtwork", noApprovalArtwork);

        return "waiting-approval-detail";
    }

    //Artwork Management
    @GetMapping("/artwork")
    public String artwork(Model model){
        List<Artwork> Artworks = contentsManagementService.getApprovedArtworks();

        model.addAttribute("artworks",Artworks);
        return "artwork";
    }
    @GetMapping("api/artwork-search")
    @ResponseBody
    public ResponseEntity<?> artworkSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<Artwork> ApprovedArtworkSearchResult = contentsManagementService.searchApprovedArtworkByKeyword(keyword);

        ApprovedArtworkDto approvedArtworkDto = new ApprovedArtworkDto(
                ApprovedArtworkSearchResult.stream().map(a -> a.getId()).collect(Collectors.toList()),
                ApprovedArtworkSearchResult.stream().map(a -> a.getCode()).collect(Collectors.toList()),
                ApprovedArtworkSearchResult.stream().map(a -> a.getArtworkName()).collect(Collectors.toList()),
                ApprovedArtworkSearchResult.stream().map(a -> a.getDeathArtist() != null ? a.getDeathArtist().getKorName() : a.getSurviveArtist().getKorName()).collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.OK).body(approvedArtworkDto);
    }


    @GetMapping("/artwork-detail")
    public String artworkDetail(@RequestParam Long artwork_id, Model model) {
        Artwork Artwork = contentsManagementService.findArtworkById(artwork_id);

        model.addAttribute("Artwork", Artwork);

        return "artwork-detail";
    }


//    @PostMapping("/new-artwork")
//    public void newArtwork(@RequestBody ArtworkDto ArtworkDto) {
//
//        BusinessManager businessManager = new BusinessManager(
//                businessManagerDto.getEmail(),
//                businessManagerDto.getName(),
//                businessManagerDto.getPermission(),
//                LocalDateTime.now(),
//                null,
//                businessManagerDto.getPhone_number()
//        );
//        patronMembersService.saveBusinessManager(businessManager);
//    }
    //Artist Management

    @GetMapping("/artist")
    public String artist(Model model){
        List<DeathArtist> deathArtists = contentsManagementService.getDeathArtists();
        List<SurviveArtist> surviveArtists = contentsManagementService.getSurviveArtists();

        if(deathArtists==null){
            model.addAttribute("surviveArtists",surviveArtists);
        }else if(surviveArtists==null){
            model.addAttribute("deathArtists",deathArtists);
        }else{
            model.addAttribute("deathArtists",deathArtists);
            model.addAttribute("surviveArtists",surviveArtists);
        }
        return "artist";
    }


    @GetMapping("/artist-detail")
    public String artistDetail(@RequestParam(required = false) Long da_id,
                               @RequestParam(required = false) Long sa_id,
                               Model model) {

        if(da_id==null){
            SurviveArtist surviveArtist = contentsManagementService.findSurviveArtistById(sa_id);
            model.addAttribute("surviveArtists",surviveArtist);
        }else if(sa_id==null){
            DeathArtist deathArtist = contentsManagementService.findDeathArtistById(da_id);
            model.addAttribute("deathArtists",deathArtist);
        }
        return "artwork-detail";
    }


    //Exhibition Management

    @GetMapping("/artist")
    public String exhibition(Model model){
        List<Exhibition> exhibitions = contentsManagementService.getExhibitions();

        model.addAttribute("exhibitions",exhibitions);
        return "exhibition";
    }


    @GetMapping("/exhibition-detail")
    public String artistDetail(@RequestParam Long exh_id,
                               Model model) {
        Exhibition exhibition = contentsManagementService.getExhibitionById(exh_id);

        model.addAttribute("exhibition",exhibition);


        return "exhibition-detail";
    }


    //Admin Management
    @PostMapping
    public void newAdminPassword(@RequestParam String password, Model model){
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        adminMembersService.setNewPasswordForAdmin(email,password);
    }

    @GetMapping("/admins")
    public String getAdminMembers(Model model) {
        List<Admin> admins = adminMembersService.getAdmins();

        model.addAttribute("admins", admins);

        return "admins";
    }


}
