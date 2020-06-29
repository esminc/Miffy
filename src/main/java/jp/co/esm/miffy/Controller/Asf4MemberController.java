package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.service.Asf4MemberService;
import jp.co.esm.miffy.service.TestResponseResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class Asf4MemberController {
  private final Asf4MemberService asf4MemberService;

  //デバッグ用メソッド
  public void hello() {
    System.out.println("Hello Spring Boot!!");
  }

  @GetMapping("asf4members")
  public String index(Model model) {
    List<Asf4Member> asf4MemberList = asf4MemberService.selectAll();
    model.addAttribute("asf4MemberList", asf4MemberList);
    System.out.println(asf4MemberService.getTestResponse());
    hello();
    return "asf4members";
  }
}
