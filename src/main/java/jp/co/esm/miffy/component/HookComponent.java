package jp.co.esm.miffy.component;

import jp.co.esm.miffy.service.hookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;

import static ajd4jp.iso.AJD310.now;

@RequiredArgsConstructor
@Component
@EnableScheduling
public class HookComponent {
    private final hookService hookService;
    private RestTemplate restTemplate;

    @Autowired
    public HookComponent(hookService hookService, RestTemplateBuilder builder) {
        this.hookService = hookService;
        this.restTemplate = builder.build();
    }

    /**
     * hookのURL
     */
    private static final String URL = "https://idobata.io/hook/custom/36145675-8b2f-4b78-bf2b-9e06577e0434";//PR用;
    // private static final String URL = "https://idobata.io/hook/custom/40fcef76-a6b7-4031-8088-50788d308b01";//debug用;

    /**
     * idobataのhookを使用して、今日の掃除当番をお知らせする。
     * 月曜から金曜の午前10時に hookのURLへPOSTリクエストをする。
     */
    @Scheduled(cron = "0 0 10 * * 1-5", zone = "Asia/Tokyo")
    public void postToHook() {
        String requestJson = hookService.makeRequest(now(ZoneId.of("Asia/Tokyo")));
        if (requestJson == null) {
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        try {
            String answer = restTemplate.postForObject(URL, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            e.printStackTrace();
        }
    }
}
