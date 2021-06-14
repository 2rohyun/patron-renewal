package com.thyme.test.mainController;

import com.thyme.test.Data.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class mainController {
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("hello", "받음?");
        model.addAttribute("a","");
        model.addAttribute("url","/");
        return "index";
    }
    @GetMapping("/member")
    public String memberView(Model model){
        Member member1 = new Member(1,"테스트1","11111111111");
        Member member2 = new Member(2,"테스트2","22222222222");
        Member member3 = new Member(3,"테스트3","33333333333");
        List<Member> memberList = new ArrayList<>();
        memberList.add(member1);
        memberList.add(member2);
        memberList.add(member3);
        Member member4 = new Member(1,"테스트4","213213213");
        model.addAttribute("memberList", memberList);
        model.addAttribute("memberObject", member4);
        return "member";
    }
    @GetMapping("/test")
    public String testForward(Model a){
        a.addAttribute("a","is forward?");
        return "forward:/member";
    }
}
