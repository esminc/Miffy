package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Asf4MemberServiceTest {

    @Autowired
    private Asf4MemberService asf4MemberService;

    @Test
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
    public void selectByIdobataIdTest() {
        // 準備
        Asf4Member expectedSelectedMember = new Asf4Member(1,"スナッフィー", "test1", "4", false);
        // 実行
        Asf4Member actualSelectedMember = asf4MemberService.selectByidobataId("test1");
        // 検証
        assertEquals(expectedSelectedMember, actualSelectedMember);
    }

    @Test
    public void updateTest() {
        // 準備
        List<Asf4Member> before = asf4MemberService.selectAll();
        before.add(new Asf4Member(4,"ポピーさん", "test4", "4", false));
        // 実行
        asf4MemberService.update(new Asf4Member(4,"ポピーさん", "test4", "4", false));
        List<Asf4Member> after = asf4MemberService.selectAll();
        // 検証
        assertEquals(before, after);
    }

    @Test
    public void deleteTest() {
        // 準備
        List<Asf4Member> before = asf4MemberService.selectAll();
        before.remove(new Asf4Member(3,"バーバラ", "test3","4", true));
        // 実行
        asf4MemberService.delete(new Asf4Member(3,"バーバラ", "test3","4", true));
        List<Asf4Member> after = asf4MemberService.selectAll();
        // 検証
        assertEquals(before, after);
    }
}
