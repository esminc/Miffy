package jp.co.esm.miffy.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Check {
    /**
     * 入力値チェックに用いる変数
     */
    private boolean errorCheck= false;

    /**
     * 確認画面で削除確認するかどうかを決める変数
     */
    private boolean deleteCheck = false;

    /**
     * 同じidobataidがDBに存在するかどうかを決める変数
     */
    private boolean idobataIdCheck = false;
}
