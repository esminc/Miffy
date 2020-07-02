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
     * フロア情報
     */
    private String floor;

    /**
     * skipチェック
     */
    private String skip;

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
