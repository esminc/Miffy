package jp.co.esm.miffy.form;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class DemoForm {

    /**
     * 名前
     */
    private String name;

    /**
     * idobataID
     */
    private String idobata_id;

    /**
     * skipチェック
     */
    private String skip;
}
