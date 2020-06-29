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

@Service
@RequiredArgsConstructor
public class Asf4MemberService {
    public final Asf4MemberRepository asf4MemberRepository;
    private RestTemplate restTemplate;

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

    public void getTestResponse() {
        try {
            String requestJson = "{\"source\":\"hello\"}";
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
