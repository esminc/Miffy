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
import java.util.NoSuchElementException;
import java.util.Optional;

import static ajd4jp.iso.AJD310.now;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class hookService {
    public final Asf4MemberRepository asf4MemberRepository;
    private RestTemplate restTemplate;

    @Autowired
    public hookService(Asf4MemberRepository asf4MemberRepository, RestTemplateBuilder builder) {
        this.asf4MemberRepository = asf4MemberRepository;
        this.restTemplate = builder.build();
    }

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
     * 前回の掃除当番の人を特定する。
     *
     * @return 前回の掃除当番をAsf4Memberクラスで返す。
     */
    private Asf4Member getLastCleaner() {
        Optional<Asf4Member> lastCleanerOptional = asf4MemberRepository.findByIsCleanerTrue();
        if (lastCleanerOptional.isPresent()) {
            return lastCleanerOptional.get();
        } else {
            throw new NoSuchElementException("検索条件:IsCleaner==true に一致する情報がありません。前回掃除した人は誰ですか。");
        }
    }

    /**
     * 今日の掃除当番の人を特定する。
     *
     * @return 掃除当番をOptionalオブジェクトで返す。
     */
    private Asf4Member getCleaner() throws NoSuchElementException {
        Asf4Member lastCleaner = getLastCleaner();
        int cleanerId = lastCleaner.getId();
        Optional<Asf4Member> cleanerOptional = asf4MemberRepository.findTopByFloorAndSkipFalseAndIdGreaterThanOrderByIdAsc("4", cleanerId);
        if (cleanerOptional.isEmpty()) {
            cleanerOptional = asf4MemberRepository.findTopByFloorAndSkipFalseOrderByIdAsc("4");
        }
        Asf4Member cleaner;
        if (cleanerOptional.isPresent()) {
            cleaner = cleanerOptional.get();
            cleaner.setCleaner(true);
            asf4MemberRepository.saveAndFlush(cleaner);
            lastCleaner.setCleaner(false);
            asf4MemberRepository.saveAndFlush(lastCleaner);
        } else {
            cleaner = null;
        }
        return cleaner;
    }

    /**
     * 祝日かどうかを判定する。
     *
     * @param date 祝日判定対象日。
     * @return 祝日ならばTRUE、祝日でなければFALSEを返す。
     */
    private boolean isHoliday(AJD date) {
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
        String postIdobataId = null;
        String mainMessage = null;
        Asf4Member cleaner;
        try {
            cleaner = getCleaner();
            if (cleaner != null) {
                postIdobataId = cleaner.getIdobataId();
                mainMessage = " 今日の掃除当番です\"}";
            } else {
                postIdobataId = "here";
                mainMessage = " 今日は誰もオフィスにいないみたい(・x・)\"}";
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
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
