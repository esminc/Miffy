package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Asf4MemberService {
    public final Asf4MemberRepository asf4MemberRepository;
    private RestTemplate restTemplate;
    /**
     * 現在の掃除当番の人を特定するID
     */
    private int cleanerTodayId = 1;

    @Autowired
    public Asf4MemberService(Asf4MemberRepository asf4MemberRepository, RestTemplateBuilder builder) {
        this.asf4MemberRepository = asf4MemberRepository;
        this.restTemplate = builder.build();
    }

    public static final String URL = "https://idobata.io/hook/custom/40767f01-0b3d-4065-8770-d9e25a206c24";

    public List<Asf4Member> selectAll() {
        List<Asf4Member> asf4MemberList = asf4MemberRepository.findAll();
        return asf4MemberList;
    }

    /**
     * 掃除当番IDを回すメソッドです
     */
    public void plusCleanId(List<Asf4Member> asf4MemberList) {
        if (cleanerTodayId == asf4MemberList.size()) {
            cleanerTodayId = 1;
        } else {
            cleanerTodayId++;
        }
    }

    /**
     * 掃除当番の人を特定するメソッドです。
     *
     * @param asf4Member
     * @return 掃除当番の人を返します。
     */
    public Asf4Member selectData(List<Asf4Member> asf4Member) {
        System.out.println(asf4Member.size());
        Optional<Asf4Member> cleaner = asf4MemberRepository.findById(cleanerTodayId);
        plusCleanId(asf4Member);
        return cleaner.get();
    }

    public void getTestResponse(String idobataid) {
        try {
            StringBuilder request = new StringBuilder();
            request.append("{\"source\":\"@");
            request.append(idobataid);
            request.append(" 今日の掃除当番です\"}");
            String requestJson = request.toString();
            // String requestJson = "{\"source\":\"idobataid\"}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
            String answer = restTemplate.postForObject(URL, entity, String.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw e;
        } catch (HttpServerErrorException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
