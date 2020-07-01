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
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class Asf4MemberService {
    public final Asf4MemberRepository asf4MemberRepository;
    private RestTemplate restTemplate;
    /**
     * 現在の掃除当番の人を特定するID
     */
    private int cleanerId = 1;

    @Autowired
    public Asf4MemberService(Asf4MemberRepository asf4MemberRepository, RestTemplateBuilder builder) {
        this.asf4MemberRepository = asf4MemberRepository;
        this.restTemplate = builder.build();
    }

    public static final String URL = "https://idobata.io/hook/custom/40767f01-0b3d-4065-8770-d9e25a206c24";

    /**
     * テーブルのデータ一覧を返すメソッドです。
     *
     * @return List型でメンバ一覧を返します。
     */
    public List<Asf4Member> selectAll() {
        List<Asf4Member> asf4MemberList = asf4MemberRepository.findAll();
        return asf4MemberList;
    }

    /**
     * 祝日かどうかを判定するメソッドです。
     *
     * @param date 祝日判定対象日。
     * @return 祝日ならばTRUE、祝日でなければFALSEを返します。
     */
    public boolean isHoliday(AJD date){
        Holiday holiday = Holiday.getHoliday(date);
        if (holiday == null) {
            return FALSE;
        }
        return TRUE;
    }

    /**
     * 掃除当番IDを回すメソッドです。
     */
    public void nextCleanerId() {
        if (cleanerId == (int) asf4MemberRepository.count()) {
            cleanerId = 1;
        }
        cleanerId++;
    }

    /**
     * 掃除当番の人を特定するメソッドです。
     *
     * @return 掃除当番の人を返します。
     */
    public Asf4Member getCleaner() {
        Optional<Asf4Member> cleaner = null;
        if (isHoliday(now(ZoneId.of("Asia/Tokyo"))) == FALSE) {
            do {
                cleaner = asf4MemberRepository.findByIdAndSkipFalse(cleanerId);
                nextCleanerId();
            } while (cleaner.isEmpty());
        }
        return cleaner.get();
    }

    /**
     * hookのURLにPOSTリクエストをするメソッドです。
     *
     * @param idobataid String型のidobataID。
     */
    public void postToHook(String idobataid){
        if (idobataid != null) {
            StringBuilder request = new StringBuilder();
            request.append("{\"source\":\"@");
            request.append(idobataid);
            request.append(" 今日の掃除当番です\"}");
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

    /**
     * 祝日、休日を除いた月〜金曜日に、idobataのhookを使用して、今日の掃除当番にお知らせをするメソッドです。
     */
    @Scheduled(cron = "0 35 14 * * 1-5", zone = "Asia/Tokyo")
    public void hook() {
        postToHook(getCleaner().getIdobataId());
    }
}
