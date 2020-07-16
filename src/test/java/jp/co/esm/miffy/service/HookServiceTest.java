package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static ajd4jp.iso.AJD310.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class HookServiceTest {

    @Autowired
    HookService hookService;

    @Test
    void getLastCleanerTest() {
        // 準備
        Asf4Member expectedCleaner = new Asf4Member(1,"スナッフィー", "test1", "4", false, true);
        // 実行
        Asf4Member actualCleaner = hookService.getLastCleaner();
        // 検証
        assertEquals(expectedCleaner, actualCleaner);
    }

    @Test
    void getCleanerTest() {
        // 準備
        List<Asf4Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(new Asf4Member(1,"スナッフィー", "test1", "4", false, false));
        expectedMembers.add(new Asf4Member(2,"ボリス", "test2", "4", false, true));
        expectedMembers.add(new Asf4Member(3,"バーバラ", "test3", "4", true, false));
        // 実行
        hookService.getCleaner();
        List<Asf4Member> actualMembers = hookService.selectAll();
        // 検証
        assertEquals(expectedMembers, actualMembers);
    }

    @Test
    void makeRequestTest() {
        // 準備
        String expectedRequest = "{\"source\":\"@test2 今日の掃除当番です\"}";
        // 実行
        String actualRequest = hookService.makeRequest(now(ZoneId.of("Asia/Tokyo")));
        // 検証
        assertEquals(expectedRequest, actualRequest);
    }
}
