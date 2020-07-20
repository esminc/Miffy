package jp.co.esm.miffy.service;

import ajd4jp.AJD;
import ajd4jp.Holiday;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
//@ExtendWith(PowerMockExtension.class)
@PrepareForTest({Holiday.class})
@SpringBootTest
class HookServiceTest {

    @Autowired
    HookService hookService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Holiday.class);
    }

    @Test
    public void makeRequestTest() {
        // 準備
        Mockito.doReturn(null).when(Holiday.getHoliday(Mockito.any(AJD.class)));
        //Mockito.when(Holiday.getHoliday(Mockito.any(AJD.class))).thenReturn(null);
        // 実行
        String actualRequest = hookService.makeRequest(now(ZoneId.of("Asia/Tokyo")));
        // 検証
        assertEquals("{\"source\":\"@test2 今日の掃除当番です\"}", actualRequest);
    }

    @Test
    public void makeRequestOnHolidayTest() {
        // 準備
        Mockito.doReturn(Holiday.UMI).when(Holiday.getHoliday(Mockito.any(AJD.class)));
        //Mockito.when(Holiday.getHoliday(now(ZoneId.of("Asia/Tokyo")))).thenReturn(Holiday.UMI);
        // 実行
        String actualRequest = hookService.makeRequest(now(ZoneId.of("Asia/Tokyo")));
        // 検証
        assertEquals("{\"source\":\"@test2 今日の掃除当番です\"}", actualRequest);
    }
}
