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
     * @return List型でメンバ一覧を返します。
     */
    public List<Asf4Member> selectAll() {
        return asf4MemberRepository.findAll();
    }

    /**
     * 今日の掃除当番の人を特定するメソッドです。
     *
     * @return 掃除当番をOptionalオブジェクトで返します。
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
     * @return 祝日ならばtrue、祝日でなければfalseを返します。
     */
    private boolean isHoliday(AJD date) {
        return Holiday.getHoliday(date) != null;
    }

    /**
     * idobataのhookを使用して、今日の掃除当番にお知らせをする。
     * 月曜から金曜の午前10時に hookのURLへPOSTリクエストをする。
     * 土曜、日曜、祝日は通知しない。
     * 4階のメンバーが全員skip==trueの場合は通知する。
     */
    @Scheduled(cron = "0 0 10 * * 1-5", zone = "Asia/Tokyo")
    private void hook() {
        if (isHoliday(now(ZoneId.of("Asia/Tokyo")))) {
            return;
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        try {
            String answer = restTemplate.postForObject(URL, entity, String.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        } catch (HttpServerErrorException e) {
            e.printStackTrace();
        }
    }
}
