package jp.co.esm.miffy.service;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import jp.co.esm.miffy.component.HolidayWrap;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest({HolidayWrap.class})
@SpringBootTest
class HookServiceTest {

    @Autowired
    HookService hookService;

    @Mock
    private HolidayWrap holidayWrap;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(HolidayWrap.class);
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void makeRequestTest() throws AJDException {
        // 準備
        AJD date = new AJD(2020, 7, 21);
        PowerMockito.doReturn(false).when(holidayWrap).isHoliday(date);
        // 実行
        String actualRequest = hookService.makeRequest(date);
        // 検証
        assertEquals("{\"source\":\"@test2 今日の掃除当番です\"}", actualRequest);
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void makeRequestOnHolidayTest() throws AJDException {
        // 準備
        AJD date = new AJD(2020, 7, 23);
        PowerMockito.doReturn(true).when(holidayWrap).isHoliday(date);
        // PowerMockito.when(holidayWrap.isHoliday(date)).thenReturn(true);
        // 実行
        String actualRequest = hookService.makeRequest(date);
        // 検証
        assertEquals(null, actualRequest);
    }

    @Test
    @Sql (statements = {"DELETE FROM members;" +
            "INSERT INTO members (name, idobata_id, skip, is_cleaner) VALUES " +
            "('スナッフィー', 'test1', TRUE, TRUE), ('ボリス', 'test2', TRUE, FALSE), ('バーバラ', 'test3', TRUE, FALSE);"})
    public void makeRequestNobodyTest() throws AJDException {
        // 準備
        AJD date = new AJD(2020, 7, 21);
        PowerMockito.doReturn(false).when(holidayWrap).isHoliday(date);
        // 実行
        String actualRequest = hookService.makeRequest(date);
        // 検証
        assertEquals("{\"source\":\"@here 今日は誰もオフィスにいないみたい(・x・)\"}", actualRequest);
    }

    @Test
    @Sql (statements = {"DELETE FROM members;" +
            "INSERT INTO members (name, idobata_id, is_cleaner) VALUES " +
            "('スナッフィー', 'test1', FALSE), ('ボリス', 'test2', FALSE), ('バーバラ', 'test3', FALSE);"})
    public void makeRequestLastCleanerIsUnknownTest() throws AJDException {
        // 準備
        AJD date = new AJD(2020, 7, 21);
        PowerMockito.doReturn(false).when(holidayWrap).isHoliday(date);
        // 実行
        String actualRequest = hookService.makeRequest(date);
        // 検証
        assertEquals("{\"source\":\"@all IsCleaner == true に一致する情報がありません。前回掃除した人は誰？(・x・)\"}", actualRequest);
    }
}
