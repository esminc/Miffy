package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
class Asf4MemberServiceTest {

    @Autowired
    private Asf4MemberService asf4MemberService;

    @Test
    @Sql({"/schema.sql", "/data.sql"})
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
    @Sql(statements = {"DELETE FROM members;"})
    public void selectAllNoData() {
        // 準備
        List<Asf4Member> expectedMembers = new ArrayList<>();
        // 実行
        List<Asf4Member> actualMembers = asf4MemberService.selectAll();
        // 検証
        assertEquals(expectedMembers, actualMembers);
    }

    @Test
    @Sql({"/schema.sql","/data.sql"})
    public void selectByIdobataIdTest() {
        // 準備
        Asf4Member expectedSelectedMember = new Asf4Member(1,"スナッフィー", "test1", "4", false);
        // 実行
        Asf4Member actualSelectedMember = asf4MemberService.selectByidobataId("test1");
        // 検証
        assertEquals(expectedSelectedMember, actualSelectedMember);
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void selectByIdobataIdException() {
        try {
            Asf4Member member = asf4MemberService.selectByidobataId("failId");
            fail();
        } catch (NoSuchElementException e) {
            assertEquals("指定のIDに一致する情報がありません。", e.getMessage());
        }
    }

    @Test
    @Sql({"/schema.sql","/data.sql"})
    public void updateTest() {
        // 準備
        List<Asf4Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(new Asf4Member(1,"ポピーさん", "test4", "4", false));
        expectedMembers.add(new Asf4Member(2,"ボリス", "test2", "4", false));
        expectedMembers.add(new Asf4Member(3,"バーバラ", "test3", "4", true));
        // 実行
        asf4MemberService.update(new Asf4Member(1,"ポピーさん", "test4", "4", false));
        List<Asf4Member> actualMembers = asf4MemberService.selectAll();
        // 検証
        assertEquals(expectedMembers, actualMembers);
    }

    @Test
    @Sql({"/schema.sql","/data.sql"})
    public void insertTest() {
        // 準備
        List<Asf4Member> expectedMembers = asf4MemberService.selectAll();
        expectedMembers.add(new Asf4Member(4,"ポピーさん", "test4", "4", false));
        // 実行
        asf4MemberService.update(new Asf4Member(4,"ポピーさん", "test4", "4", false));
        List<Asf4Member> actualMembers = asf4MemberService.selectAll();
        // 検証
        assertEquals(expectedMembers, actualMembers);
    }
}
