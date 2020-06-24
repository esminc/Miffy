package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class Asf4MemberController {

  @Autowired
  Asf4MemberRepository asf4MemberRepository;

  @RequestMapping(value = "asf4members", method = RequestMethod.GET)
  public String index(Model model) {
    List<Asf4Member> asf4MemberList=asf4MemberRepository.findAll();
    model.addAttribute("asf4MemberList", asf4MemberList);
    return "asf4members";
  }
}
