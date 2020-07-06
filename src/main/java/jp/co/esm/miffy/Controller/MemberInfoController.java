package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.NullServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.NoSuchElementException;
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
    public String search(Asf4Member asf4Member) {
        return "search";
    }

    /**
     * 再検索画面に遷移する
     *
     * @param asf4Member Formオブジェクト
     * @return 検索画面へのパス
     */
    @RequestMapping("/research")
    public String research(Asf4Member asf4Member) {
        return "research";
    }

    /**
     * 確認画面に遷移する
     *
     * @param asf4Member Formオブジェクト
     * @return 確認画面へのパス
     */
    @RequestMapping("/confirm")
    public String confirm(Asf4Member asf4Member) {
        try {
            Optional<Asf4Member> asf4MemberOptional = asf4MemberService.selectByidobataId(asf4Member.getIdobataId());
            //null checkあとで
            Asf4Member result = asf4MemberOptional.orElse(null);
            asf4Member.setId(result.getId());
            asf4Member.setName(result.getName());
            asf4Member.setIdobataId(result.getIdobataId());
            asf4Member.setFloor(result.getFloor());
            asf4Member.setSkip(result.isSkip());
            return "confirm";
        } catch (NullPointerException e) {
            System.out.println("エラーをcatchしました");
            return "research";
        }
    }

    /**
     * 削除画面に遷移する
     *
     * @param asf4Member Formオブジェクト
     * @return 検索画面へのパス
     */
    @RequestMapping("/delete")
    public String delete(Asf4Member asf4Member) {
        asf4MemberService.delete(asf4Member);
        return "delete";
    }

    /**
     * 登録画面に遷移する
     *
     * @return 登録画面へのパス
     */
    @RequestMapping("/update")
    public String update() {
        return "update";
    }

    /**
     * 完了画面に遷移する
     *
     * @return 完了画面へのパス
     */
    @RequestMapping("/complete")
    public String complete(Asf4Member asf4Member) {
        System.out.println(asf4Member.getName());
        try {
            asf4MemberService.update(asf4Member);
            return "complete";
        } catch (NullPointerException e) {
            System.out.println("エラーをcatchしました");
            return "search";
        }
    }
}
