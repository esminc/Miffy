package jp.co.esm.miffy.service;

import jp.co.esm.miffy.MiffyApplication;
import jp.co.esm.miffy.entity.Asf4Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Asf4MemberServiceTest {
    @Autowired
    Asf4MemberService asf4MemberService;

    @Test
    void selectAllTest() {
        // 準備
        List<Asf4Member> expectedAsd4Members = new ArrayList<>();
        expectedAsd4Members.add(new Asf4Member(1,"永和 一郎", "here"));
        expectedAsd4Members.add(new Asf4Member(2,"永和 二郎", "all"));
        expectedAsd4Members.add(new Asf4Member(3,"永和 三郎", "test"));

        // 実行
        List<Asf4Member> actualAsf4Members = asf4MemberService.selectAll();

        // 検証
        assertEquals(expectedAsd4Members, actualAsf4Members);

        //fail();
    }
}
