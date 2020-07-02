package jp.co.esm.miffy.form;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DemoForm {

    /**
     * id
     */
    private Integer id;

    /**
     * 名前
     */
    private String name;

    /**
     * idobataID
     */
    private String idobata_id;

    /**
     * フロア情報
     */
    private String floor;

    /**
     * skipチェック
     */
    private Boolean skip;

    /**
     *
     * @return Map<String,String>型のフロアオブジェクトを返す
     */
    public Map<String, String> getFloorItems() {
        Map<String, String> floorMap = new LinkedHashMap<String, String>();
        for (int i = 1; i <= 4; i++) {
            floorMap.put(String.valueOf(i), String.valueOf(i));
        }
        return floorMap;
    }
}
