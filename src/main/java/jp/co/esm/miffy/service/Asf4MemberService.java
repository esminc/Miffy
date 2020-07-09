package jp.co.esm.miffy.service;

import ajd4jp.AJD;
import ajd4jp.Holiday;
import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static ajd4jp.iso.AJD310.now;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class Asf4MemberService {
    public final Asf4MemberRepository asf4MemberRepository;
    private RestTemplate restTemplate;

    @Autowired
    public Asf4MemberService(Asf4MemberRepository asf4MemberRepository, RestTemplateBuilder builder) {
        this.asf4MemberRepository = asf4MemberRepository;
        this.restTemplate = builder.build();
    }

    /**
     * 現在の掃除当番の人を特定するID
     */
    private int cleanerId = 0;

    /**
     * hookのURL
     */
    private static final String URL = "https://idobata.io/hook/custom/36145675-8b2f-4b78-bf2b-9e06577e0434";//PR用;
    //private static final String URL = "https://idobata.io/hook/custom/40fcef76-a6b7-4031-8088-50788d308b01";//debug用;

    /**
     * テーブルのデータ一覧を返す。
     *
     * @return List型でメンバ一覧を返す。
     */
    public List<Asf4Member> selectAll() {
        return asf4MemberRepository.findAll();
    }

    /**
     * 今日の掃除当番の人を特定する。
     *
     * @return 掃除当番をOptionalオブジェクトで返す。
     */
    private Optional<Asf4Member> getCleaner() {
        Optional<Asf4Member> cleaner = asf4MemberRepository.findTopByFloorAndSkipFalseAndIdGreaterThanOrderByIdAsc("4", cleanerId);
        if (cleaner.isEmpty()) {
            cleaner = asf4MemberRepository.findTopByFloorAndSkipFalseOrderByIdAsc("4");
        }
        return cleaner;
    }

    /**
     * 祝日かどうかを判定する。
     *
     * @param date 祝日判定対象日。
     * @return 祝日ならばTRUE、祝日でなければFALSEを返す。
     */
    public boolean isHoliday(AJD date) {
        return Holiday.getHoliday(date) != null;
    }

    /**
     * 曜日・日付に応じたメンション付きの掃除当番通知用リクエスト文を生成する。
     * 該当するメンバーが誰もいない場合は本文にその旨を記載する。
     *
     * @return hookのURLへPOSTリクエストするJSON形式テキストを返す。祝日はnullを返す。
     */
     private String makeRequest(AJD date) {
        if (isHoliday(date)) {
            return null;
        }
        String postIdobataId;
        String mainMessage;
        Optional<Asf4Member> cleaner = getCleaner();
        if (cleaner.isPresent()) {
            postIdobataId = cleaner.get().getIdobataId();
            mainMessage = " 今日の掃除当番です\"}";
            cleanerId = cleaner.get().getId();
        } else {
            postIdobataId = "here";
            mainMessage = " 今日はだれも掃除できません(・x・)だれか来る？\"}";
        }
        StringBuilder request = new StringBuilder();
        request.append("{\"source\":\"@");
        request.append(postIdobataId);
        request.append(mainMessage);
        String requestJson = request.toString();
        return requestJson;
    }

    /**
     * idobataのhookを使用して、今日の掃除当番をお知らせする。
     * 月曜から金曜の午前10時に hookのURLへPOSTリクエストをする。
     */
    @Scheduled(cron = "0 30 16-17 * * 1-5", zone = "Asia/Tokyo")
    public void postToHook() {
        String requestJson = makeRequest(now(ZoneId.of("Asia/Tokyo")));
        if (requestJson == null) {
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        try {
            String answer = restTemplate.postForObject(URL, entity, String.class);
        } catch (
            HttpClientErrorException e) {
            e.printStackTrace();
        } catch (HttpServerErrorException e) {
            e.printStackTrace();
        }
    }
}
