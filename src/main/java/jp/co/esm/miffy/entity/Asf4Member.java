package jp.co.esm.miffy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "members")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asf4Member {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "idobata_id")
  private String idobataId;

  @Column(name = "floor")
  private String floor;

  @Column(name = "skip")
  private boolean skip;

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
