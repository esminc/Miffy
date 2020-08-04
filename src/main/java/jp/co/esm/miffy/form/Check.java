package jp.co.esm.miffy.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Check {
    /**
     * 入力値チェックに用いる変数
     */
    private String errorCheck;

    /**
     * 確認画面で削除確認するかどうかを決める変数
     */
    private String deleteCheck = "off";

    /**
     * 同じidobataidがDBに存在するかどうかを決める変数
     */
    private String idobataIdCheck = "off";
}
