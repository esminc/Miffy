package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.component.HookComponent;
import jp.co.esm.miffy.entity.Asf4Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class Asf4MemberController {
    private final jp.co.esm.miffy.service.HookService hookService;
    private final HookComponent hookComponent;

    @GetMapping("asf4members")
    public String index(Model model) {
        List<Asf4Member> asf4MemberList = hookService.selectAll();
        model.addAttribute("asf4MemberList", asf4MemberList);
        //デバッグ用
        //hookComponent.postToHook();
        return "asf4members";
    }
}
