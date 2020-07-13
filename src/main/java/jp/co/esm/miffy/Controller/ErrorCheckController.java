
package jp.co.esm.miffy.Controller;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.form.ErrorCheck;
import jp.co.esm.miffy.service.Asf4MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@SessionAttributes(types = ErrorCheck.class)
public class ErrorCheckController {
    private final Asf4MemberService asf4MemberService;
    /**
     * 入力フォームをリセットするかどうかを判定する変数
     */
    private boolean formReset = true;

    /**
     * entityオブジェクトを初期化して返却する
     *
     * @return Formオブジェクト
     */
    @ModelAttribute("errorCheck")
    public ErrorCheck createErrorCheck() {
        ErrorCheck errorCheck = new ErrorCheck();
        return errorCheck;
    }
}


