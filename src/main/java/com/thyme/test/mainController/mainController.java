package com.thyme.test.mainController;

import com.thyme.test.Data.Members;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class mainController {

    @GetMapping("/")
    public String index(Model model){

        model.addAttribute("hello", "hi");
        return "index";
    }

    @GetMapping("/member")
    public String memberView(Model model){
        Members member1 = new Members(1,"테스트1","11111111111");
        Members member2 = new Members(2,"테스트2","22222222222");
        Members member3 = new Members(3,"테스트3","33333333333");
        List<Members> memberLIST = new ArrayList<>();
        memberLIST.add(member1);
        memberLIST.add(member2);
        memberLIST.add(member3);
        model.addAttribute("memberLIST", memberLIST);
        return "member";
    }




}
