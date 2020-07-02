package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.form.DemoForm;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@SessionAttributes(types = DemoForm.class)
public class DemoController {
    private final Asf4MemberService asf4MemberService;

    /**
     * Formオブジェクトを初期化して返却する
     *
     * @return Formオブジェクト
     */
    @ModelAttribute("demoForm")
    public DemoForm createDemoForm() {
        DemoForm demoForm = new DemoForm();
        /**
         * フォームの初期値を設定する
         */
        demoForm.setName("");
        demoForm.setFloor("3");
        demoForm.setSkip(Boolean.FALSE);
        return demoForm;
    }

    /**
     * 検索画面に遷移する
     *
     * @param demoForm Formオブジェクト
     * @return 検索画面へのパス
     */
    @RequestMapping("/")
    public String index(DemoForm demoForm) {
        return "search";
    }

    /**
     * 確認画面に遷移する
     *
     * @param demoForm Formオブジェクト
     * @return 確認画面へのパス
     */
    @RequestMapping("/confirm")
    public String confirm(DemoForm demoForm) {
        Optional<Asf4Member> asf4MemberOptional = asf4MemberService.selectByidobataId(demoForm.getIdobata_id());
        //null checkあとで
        demoForm.setId(asf4MemberOptional.get().getId());
        demoForm.setName(asf4MemberOptional.get().getName());
        demoForm.setIdobata_id(asf4MemberOptional.get().getIdobataId());
        demoForm.setFloor(asf4MemberOptional.get().getFloor());
        demoForm.setSkip(asf4MemberOptional.get().isSkip());
        return "confirm";
    }

    /**
     * 登録画面に遷移する
     *
     * @return 登録画面へのパス
     */
    @RequestMapping("/update")
    public String Search() {
        return "update";
    }

    /**
     * 完了画面に遷移する
     *
     * @return 完了画面へのパス
     */
    @RequestMapping("/complete")
    public String send(DemoForm demoForm) {
        Asf4Member asf4Member = new Asf4Member();
        asf4Member.setId(demoForm.getId());
        asf4Member.setName(demoForm.getName());
        asf4Member.setIdobataId(demoForm.getIdobata_id());
        asf4Member.setFloor(demoForm.getFloor());
        asf4Member.setSkip(demoForm.getSkip());
        System.out.println(asf4Member.getName());
        asf4MemberService.update(asf4Member);
        return "complete";
    }
}
