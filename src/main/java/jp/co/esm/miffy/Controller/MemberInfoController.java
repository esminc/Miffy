package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.component.HookComponent;
import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.form.Check;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Setter
@Getter
@Controller
@SessionAttributes(types = {Asf4Member.class, Check.class})
@RequiredArgsConstructor
public class MemberInfoController {
    private final jp.co.esm.miffy.service.HookService hookService;
    private final Asf4MemberService asf4MemberService;
    private final HookComponent hookComponent;

    /**
     * entityオブジェクト"asf4Member"のIdにnullをsetすることで、新規登録の際に、idを指定せずにupdate()を
     * 実行するようにしている
     *
     * @param model
     * @return メンバ登録画面のパス
     */
    @RequestMapping("/asf4members")
    public String index(Model model) {
        Asf4Member asf4Member = new Asf4Member();
        Check check = new Check();
        List<Asf4Member> asf4MemberList = asf4MemberService.selectAll();
        model.addAttribute("check", check);
        model.addAttribute("asf4Member", asf4Member);
        model.addAttribute("asf4MemberList", asf4MemberList);
        return "asf4members";
    }

    /**
     * 今日は掃除できないボタンが押された時に、次の掃除当番に通知
     * @return メンバ登録画面のパス
     */
    @RequestMapping("/skip")
    public String skip() {
        hookComponent.postToHook();
        return "redirect:/asf4members";
    }

    /**
     * entityオブジェクト"asf4Member"のidobataIdに文字列"no"をsetすることで、検索画面でエラーメッセージが表示されないようにしている。
     *
     * @param asf4Member entityオブジェクト
     * @return 検索画面のパス
     */
    @RequestMapping("/search")
    public String search(@ModelAttribute Asf4Member asf4Member, @ModelAttribute Check check, Model model) {
        model.addAttribute("asf4Member", asf4Member);
        model.addAttribute("check", check);
        return "search";
    }

    /**
     * 検索をして、テーブルに一致する項目がある場合は、確認画面に遷移する。
     * @param asf4Member entityオブジェクト
     * @param check formオブジェクト
     * @param model
     * @return 確認画面か検索画面のパス
     */
    @RequestMapping("/confirm")
    public String confirm(@ModelAttribute Asf4Member asf4Member, @ModelAttribute Check check, Model model) {
        try {
            Asf4Member asf4MemberOptional = asf4MemberService.selectByidobataId(asf4Member.getIdobataId());
            asf4Member.setId(asf4MemberOptional.getId());
            asf4Member.setName(asf4MemberOptional.getName());
            asf4Member.setIdobataId(asf4MemberOptional.getIdobataId());
            asf4Member.setSkip(asf4MemberOptional.isSkip());
            asf4Member.setNote(asf4MemberOptional.getNote());
            model.addAttribute("asf4Member", asf4Member);
            return "confirm";
        } catch (NoSuchElementException e) {
            check.setErrorCheck(true);
            model.addAttribute("check", check);
            System.out.println("エラーをcatchしました");
            return "search";
        }
    }

    /**
     * @param asf4Member entityオブジェクト
     * @return 削除確認画面のパス
     */
    @RequestMapping("/confirm_delete")
    public String confirmDelete(Asf4Member asf4Member) {
        return "confirm_delete";
    }

    /**
     * @param asf4Member entityオブジェクト
     * @return メンバ登録画面へのパス
     */
    @RequestMapping("/delete")
    public String delete(Asf4Member asf4Member) {
        asf4MemberService.delete(asf4Member);
        return "redirect:/asf4members";
    }

    /**
     * 新規登録する際に、entityの情報を初期化する(IDは指定しないようにする)
     *
     * @param asf4Member entityオブジェクト
     * @return 登録画面のパス
     */
    @RequestMapping("/create")
    public String create(@ModelAttribute Asf4Member asf4Member, @ModelAttribute Check check, Model model) {
        model.addAttribute("asf4Member", asf4Member);
        model.addAttribute("check", check);
        return "update";
    }

    /**
     * @param asf4Member entityオブジェクト
     * @param model
     * @return 登録画面のパス
     */
    @RequestMapping("/update")
    public String update(@ModelAttribute Asf4Member asf4Member, @ModelAttribute Check check, Model model) {
        model.addAttribute("asf4Member", asf4Member);
        return "update";
    }

    /**
     * idobataIdを更新したり、登録するときにidobataIdがすでに存在する場合は、登録画面でエラーを表示する。
     * todo: idobataIdを主キーにして被りをなくす
     * @param asf4Member entityオブジェクト
     * @param check formオブジェクト
     * @param model
     * @return 登録画面のパスかデータ一覧画面のパスをリダイレクト先として指定したもの
     */
    @RequestMapping("/complete")
    public String complete(@ModelAttribute Asf4Member asf4Member, @ModelAttribute Check check, Model model) {
        // idobtaIDがDBに存在しない時は、catch節に移る
        try {
            Asf4Member asf4MemberOptional = asf4MemberService.selectByidobataId(asf4Member.getIdobataId());
            // idobataID以外の情報を更新する時はデータを更新する
            if (asf4Member.getId() == asf4MemberOptional.getId()) {
                asf4MemberService.update(asf4Member);
                model.addAttribute("asf4Member", asf4Member);
                model.addAttribute("check", check);
            } else {
                check.setIdobataIdCheck(true);
                model.addAttribute("asf4Member", asf4Member);
                model.addAttribute("check", check);
                return "update";
            }
        } catch (NoSuchElementException e) {
            asf4MemberService.update(asf4Member);
            model.addAttribute("asf4Member", asf4Member);
            model.addAttribute("check", check);
        }
        return "redirect:/asf4members";
    }
}
