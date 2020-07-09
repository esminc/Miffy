package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
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
     * entityオブジェクトを初期化して返却する
     *
     * @return entityオブジェクト
     */
    @ModelAttribute("asf4Member")
    public Asf4Member createAsf4Member() {
        Asf4Member asf4Member = new Asf4Member();
        return asf4Member;
    }

    /**
     * 検索画面に遷移する
     * entityオブジェクト"asf4Member"のidobataIdに文字列"no"をsetすることで、検索画面でエラーメッセージが表示されないようにしている。
     *
     * @param asf4Member entityオブジェクト
     * @return 検索画面へのパス
     */
    @RequestMapping("/search")
    public String search(Asf4Member asf4Member) {
        asf4Member.setName("no");
        return "search";
    }

    /**
     * 確認画面か検索画面に遷移する
     * 検索をして、テーブルに一致する項目がある場合は、確認画面に遷移する。
     * 検索をして、テーブルに一致する項目がない場合は、entityオブジェクト"asf4Member"のidobataIdに文字列"Yes"をsetすることで、
     * エラーメッセージ付きの検索画面に遷移する
     *
     * @param asf4Member entityオブジェクト
     * @return 確認画面か検索画面へのパス
     */
    @RequestMapping("/confirm")
    public String confirm(Asf4Member asf4Member) {
        try {
            Asf4Member result = asf4MemberService.selectByidobataId(asf4Member.getIdobataId());
            asf4Member.setId(result.getId());
            asf4Member.setName(result.getName());
            asf4Member.setIdobataId(result.getIdobataId());
            asf4Member.setFloor(result.getFloor());
            asf4Member.setSkip(result.isSkip());
            return "confirm";
        } catch (NoSuchElementException e) {
            System.out.println("エラーをcatchしました");
            asf4Member.setName("Yes");
            return "search";
        }
    }

    /**
     * 削除確認画面に遷移する
     *
     * @param asf4Member entityオブジェクト
     * @return 削除確認画面へのパス
     */
    @RequestMapping("/delete-confirm")
    public String deleteConfirm(Asf4Member asf4Member) {
        return "delete-confirm";
    }

    /**
     * 削除画面に遷移する
     *
     * @param asf4Member entityオブジェクト
     * @return 削除画面へのパス
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
        asf4MemberService.update(asf4Member);
        return "complete";
    }
}
