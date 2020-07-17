package jp.co.esm.miffy.service;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class HookServiceTest {

    @Autowired
    HookService hookService;

    @Test
    public void makeRequest() throws AJDException {
        // 準備

        // 実行
        String actualRequest = hookService.makeRequest(new AJD(2020, 7, 17));
        // 検証
        assertEquals("{\"source\":\"@test2 今日の掃除当番です\"}", actualRequest);
    }
}
