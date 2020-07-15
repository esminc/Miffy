package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.service.HookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class Asf4MemberController {
    private final HookService hookService;

    @GetMapping("asf4members")
    public String index(Model model) {
        List<Asf4Member> asf4MemberList = hookService.selectAll();
        model.addAttribute("asf4MemberList", asf4MemberList); // debug用
        hookService.postToHook();
        return "asf4members";
    }
}
