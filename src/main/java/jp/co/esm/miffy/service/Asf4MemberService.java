package jp.co.esm.miffy.service;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Holiday;
import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
     * 掃除当番IDを回すメソッドです。
     */
    public void nextCleanerId() {
        if (cleanerId == (int) asf4MemberRepository.count()) {
            cleanerId = 1;
        } else {
            cleanerId++;
        }
    }

    /**
     * 掃除当番の人を特定するメソッドです。
     *
     * @return 掃除当番の人を返します。
     */
    public Asf4Member getCleaner() {
        Optional<Asf4Member> cleaner = null;
        try {
            if (isHoliday(now(ZoneId.systemDefault())) == FALSE) {
                do {
                    cleaner = asf4MemberRepository.findByIdAndSkipFalse(cleanerId);
                    nextCleanerId();
                } while (cleaner.isEmpty());
            }
        } catch (AJDException e) {
            e.printStackTrace();
        }
        return cleaner.get();
    }

    /**
     * hookのURLにPOSTリクエストをするメソッドです。
     *
     * @param idobataid int型のidobataID
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

    public boolean isHoliday(AJD date) throws AJDException {
        Holiday holiday = Holiday.getHoliday(date);
        if (holiday == null) {
            return FALSE;
        }
        return TRUE;
    }
}
