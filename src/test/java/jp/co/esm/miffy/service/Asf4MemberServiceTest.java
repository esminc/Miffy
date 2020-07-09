package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Asf4MemberServiceTest {

    @Autowired
    private Asf4MemberService asf4MemberService;

    @Test
/*    @Sql(statements = {"DROP TABLE members;",
            "CREATE TABLE if not exists members " +
                    "(id SERIAL NOT NULL PRIMARY KEY, name VARCHAR(255) NOT NULL, idobata_id VARCHAR(20), floor VARCHAR(20) DEFAULT 4, skip BOOLEAN DEFAULT FALSE);",
            "INSERT INTO members (name, idobata_id, skip) VALUES " +
                    "('スナッフィー', 'test1', FALSE), " +
                    "('ボリス', 'test2', FALSE)," +
                    "('バーバラ', 'test3', TRUE);"})*/
    public void selectAllTest() {
        // 準備
        List<Asf4Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(new Asf4Member(1,"スナッフィー", "test1", "4", false));
        expectedMembers.add(new Asf4Member(2,"ボリス", "test2", "4", false));
        expectedMembers.add(new Asf4Member(3,"バーバラ", "test3", "4", true));
        // 実行
        List<Asf4Member> actualMembers = asf4MemberService.selectAll();
        // 検証
        assertEquals(expectedMembers, actualMembers);
    }

    @Test
    @Sql(statements = {"DROP TABLE members;",
            "CREATE TABLE if not exists members " +
                    "(id SERIAL NOT NULL PRIMARY KEY, name VARCHAR(255) NOT NULL, idobata_id VARCHAR(20), floor VARCHAR(20) DEFAULT 4, skip BOOLEAN DEFAULT FALSE);",
            "INSERT INTO members (name, idobata_id, skip) VALUES " +
                    "('スナッフィー', 'test1', FALSE), " +
                    "('ボリス', 'test2', FALSE)," +
                    "('バーバラ', 'test3', TRUE);"})
    public void selectByIdobataIdTest() {
        // 準備
        Asf4Member expectedSelectedMember = new Asf4Member(1,"スナッフィー", "test1", "4", false);
        // 実行
        Asf4Member actualSelectedMember = asf4MemberService.selectByidobataId("test1");
        // 検証
        assertEquals(expectedSelectedMember, actualSelectedMember);
    }

}
