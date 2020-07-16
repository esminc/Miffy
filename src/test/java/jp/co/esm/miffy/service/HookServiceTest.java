package jp.co.esm.miffy.service;

import ajd4jp.AJD;
import ajd4jp.Holiday;
import jp.co.esm.miffy.entity.Asf4Member;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static ajd4jp.iso.AJD310.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@Transactional
class HookServiceTest {

    @Mock
    Holiday mockHoliday;

    @InjectMocks
    @Autowired
    private HookService hookService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

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
    void isHolidayTest() {
        // 準備
        AJD today = now(ZoneId.of("Asia/Tokyo"));
        when(mockHoliday.getHoliday(today)).thenReturn(null);
        // 実行
        boolean actual = hookService.isHoliday(today);
        // 検証
        assertEquals(false, actual);
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
