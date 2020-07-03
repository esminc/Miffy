package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@SessionAttributes(types = Asf4Member.class)
public class MemberInfoController {
    private final Asf4MemberService asf4MemberService;

    /**
     * Formオブジェクトを初期化して返却する
     *
     * @return Formオブジェクト
     */
    @ModelAttribute("asf4Member")
    public Asf4Member createAsf4Member() {
        Asf4Member asf4Member = new Asf4Member();
        /**
         * フォームの初期値を設定する
         */
        asf4Member.setName("");
        asf4Member.setFloor("3");
        asf4Member.setSkip(Boolean.FALSE);
        return asf4Member;
    }

    /**
     * 検索画面に遷移する
     *
     * @param asf4Member Formオブジェクト
     * @return 検索画面へのパス
     */
    @RequestMapping("/")
    public String index(Asf4Member asf4Member) {
        return "search";
    }

    /**
     * 確認画面に遷移する
     *
     * @param asf4Member Formオブジェクト
     * @return 確認画面へのパス
     */
    @RequestMapping("/confirm")
    public String confirm(Asf4Member asf4Member) {
        Optional<Asf4Member> asf4MemberOptional = asf4MemberService.selectByidobataId(asf4Member.getIdobataId());
        //null checkあとで
        asf4Member.setId(asf4MemberOptional.get().getId());
        asf4Member.setName(asf4MemberOptional.get().getName());
        asf4Member.setIdobataId(asf4MemberOptional.get().getIdobataId());
        asf4Member.setFloor(asf4MemberOptional.get().getFloor());
        asf4Member.setSkip(asf4MemberOptional.get().isSkip());
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
    public String send(Asf4Member asf4Member) {
        System.out.println(asf4Member.getName());
        asf4MemberService.update(asf4Member);
        return "complete";
    }
}
