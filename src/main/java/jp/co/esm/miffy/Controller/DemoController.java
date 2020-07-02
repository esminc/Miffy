package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.form.DemoForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(types = DemoForm.class)
public class DemoController {
    /**
     * Formオブジェクトを初期化して返却する
     *
     * @return Formオブジェクト
     */
    @ModelAttribute("demoForm")
    public DemoForm createDemoForm() {
        DemoForm demoForm = new DemoForm();
        /**
         * 名前の初期値を設定する
         */
        demoForm.setName("");
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
        return "confirm";
    }

    /**
     * 登録画面に遷移する
     *
     * @param demoForm Formオブジェクト
     * @return 登録画面へのパス
     */
    @RequestMapping("/update")
    public String Search(DemoForm demoForm) {
        return "update";
    }

    /**
     * 完了画面に遷移する
     *
     * @return 完了画面へのパス
     */
    @RequestMapping("/complete")
    public String send() {
        return "complete";
    }
}
