package jp.co.esm.miffy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "members")
public class Asf4Member {

  @Id
  @Column(name = "number")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer number;
  private String name;

  @Column(name = "idobata_id")
  private String idobataId;

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdobataId() {
    return idobataId;
  }

  public void setIdobataId(String idobataId) {
    this.idobataId = idobataId;
  }
}
