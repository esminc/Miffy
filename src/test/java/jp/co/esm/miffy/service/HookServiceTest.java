package jp.co.esm.miffy.service;

import ajd4jp.Holiday;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;

import static ajd4jp.iso.AJD310.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class HookServiceTest {

    @Mock
    Holiday holiday;

    @InjectMocks
    @Autowired
    HookService hookService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void makeRequestTest() {
        // 準備
        when(holiday.getHoliday(now(ZoneId.of("Asia/Tokyo")))).thenReturn(null);
        // 実行
        String actualRequest = hookService.makeRequest(now(ZoneId.of("Asia/Tokyo")));
        // 検証
        assertEquals("{\"source\":\"@test2 今日の掃除当番です\"}", actualRequest);
    }
}
