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
class HookServiceTest {

    @Autowired
    HookService hookService;

    @Test
    @Sql({"/schema.sql","/data.sql"})
    void selectAll() {
        // 準備
        List<Asf4Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(new Asf4Member(1,"スナッフィー", "test1", "4", false, true));
        expectedMembers.add(new Asf4Member(2,"ボリス", "test2", "4", false, false));
        expectedMembers.add(new Asf4Member(3,"バーバラ", "test3", "4", true, false));
        // 実行
        List<Asf4Member> actualMembers = hookService.selectAll();
        // 検証
        assertEquals(expectedMembers, actualMembers);
    }

    @Test
    void getCleanerTest() {
        // 準備
        Asf4Member expectedCleaner = new Asf4Member(2,"ボリス", "test2", "4", false, false);
        // 実行
        Asf4Member actualCleaner = hookService.getCleaner();
        // 検証
        assertEquals(expectedCleaner, actualCleaner);
    }

    @Test
    void isHolidayTest() {

    }
}
