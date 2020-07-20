package jp.co.esm.miffy.service;

import jp.co.esm.miffy.component.HollidayWrap;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;

import static ajd4jp.iso.AJD310.now;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest({HollidayWrap.class})
@SpringBootTest
class HookServiceTest {

    @Autowired
    HookService hookService;

    @Mock
    private HollidayWrap hollidayWrap;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(HollidayWrap.class);
    }

    @Test
    public void makeRequestTest() {
        // 準備
        Mockito.doReturn(false).when(hollidayWrap).isHoliday(now(ZoneId.of("Asia/Tokyo")));
        // 実行
        String actualRequest = hookService.makeRequest(now(ZoneId.of("Asia/Tokyo")));
        // 検証
        assertEquals("{\"source\":\"@test2 今日の掃除当番です\"}", actualRequest);
    }

    @Test
    public void makeRequestOnHolidayTest() {
        // 準備
        Mockito.doReturn(true).when(hollidayWrap).isHoliday(now(ZoneId.of("Asia/Tokyo")));
        // 実行
        String actualRequest = hookService.makeRequest(now(ZoneId.of("Asia/Tokyo")));
        // 検証
        assertEquals(null, actualRequest);
    }
}
