package jp.co.esm.miffy.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@Transactional
@Sql("test-data.sql")
class HookServiceTest {

    @Autowired
    private HookService hookService;

    @Test
    public void makeMainMessageUsually() {
        String expectedMessage = "test1 今日の掃除当番です";
        String actualMessage = hookService.makeMainMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Sql(statements = "UPDATE members SET skip = TRUE WHERE id < 3;")
    public void makeMainMessageEveryoneSkipTrue() {
        String expectedMessage = "here cleaner == true かつ skip == false に一致する情報がありません。今日はみんなお掃除できないみたい(・x・)";
        String actualMessage = hookService.makeMainMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
