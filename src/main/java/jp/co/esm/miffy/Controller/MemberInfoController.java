package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.form.Check;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Setter
@Getter
@Controller
@RequiredArgsConstructor
@SessionAttributes(types = {Asf4Member.class, Check.class})
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
     * データ一覧画面に遷移する
     * entityオブジェクト"asf4Member"のIdにnullをsetすることで、新規登録の際に、idを指定せずにupdate()を
     * 実行するようにしている
     *
     * @param model
     * @param asf4Member
     * @return
     */
    @RequestMapping("asf4members")
    public String index(Model model, Asf4Member asf4Member, Check check) {
        asf4Member.setId(null);
        List<Asf4Member> asf4MemberList = asf4MemberService.selectAll();
        model.addAttribute("asf4MemberList", asf4MemberList);
        // asf4MemberService.hook();    // デバッグ用のhookメソッドの呼び出し
        return "asf4members";
    }

    /**
     * 検索画面に遷移する
     * entityオブジェクト"asf4Member"のidobataIdに文字列"no"をsetすることで、検索画面でエラーメッセージが表示されないようにしている。
     *
     * @param asf4Member entityオブジェクト
     * @return 検索画面へのパス
     */
    @RequestMapping("/search")
    public String search(Asf4Member asf4Member,Check check) {
        check.setErrorCheck("no");
        asf4Member.setIdobataId("");
        check.setDeleteCheck("off");
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
    public String confirm(Asf4Member asf4Member, Check check) {
        try {
            Asf4Member asf4MemberOptional = asf4MemberService.selectByidobataId(asf4Member.getIdobataId());
            asf4Member.setId(asf4MemberOptional.getId());
            asf4Member.setName(asf4MemberOptional.getName());
            asf4Member.setIdobataId(asf4MemberOptional.getIdobataId());
            asf4Member.setFloor(asf4MemberOptional.getFloor());
            asf4Member.setSkip(asf4MemberOptional.isSkip());
            return "confirm";
        } catch (NoSuchElementException e) {
            check.setErrorCheck("Yes");
            System.out.println("エラーをcatchしました");
            return "search";
        }
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
        return "redirect:/asf4members";
    }

    /**
     * 新規登録する際に、entityの情報を初期化する(IDは指定しないようにする)
     *
     * @param asf4Member
     * @return 登録画面へのパス
     */
    @RequestMapping("/create")
    public String create(Asf4Member asf4Member) {
        asf4Member.setId(null);
        asf4Member.setName("");
        asf4Member.setIdobataId("");
        asf4Member.setFloor("");
        asf4Member.setSkip(false);
        return "update";
    }

    /**
     * 登録画面に遷移する
     *
     * @return 登録画面へのパス
     */
    @RequestMapping("/update")
    public String update(Asf4Member asf4Member) {
        return "update";
    }

    /**
     * 完了画面に遷移する
     *
     * @return データ一覧画面URLをリダイレクト先として指定したもの
     */
    @RequestMapping("/complete")
    public String complete(Asf4Member asf4Member, Check check) {
        try {
            Asf4Member asf4MemberOptional = asf4MemberService.selectByidobataId(asf4Member.getIdobataId());
            if (asf4Member.getId() == asf4MemberOptional.getId()) {
                check.setIdobataIdCheck("off");
                asf4MemberService.update(asf4Member);
            } else {
                check.setIdobataIdCheck("Yes");
            }
        } catch (NoSuchElementException e) {
            check.setIdobataIdCheck("off");
            asf4MemberService.update(asf4Member);
        }
        return "redirect:/asf4members";
    }
}
