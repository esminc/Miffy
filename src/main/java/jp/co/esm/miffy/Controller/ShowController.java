package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class ShowController {
    private final Asf4MemberService asf4MemberService;

    @GetMapping("asf4members")
    public String index(Model model) {
        List<Asf4Member> asf4MemberList = asf4MemberService.selectAll();
        model.addAttribute("asf4MemberList", asf4MemberList);
        // asf4MemberService.hook();    // デバッグ用のhookメソッドの呼び出し
        return "asf4members";
    }
}
