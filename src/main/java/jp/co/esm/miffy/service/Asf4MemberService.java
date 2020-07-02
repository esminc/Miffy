package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class Asf4MemberService {
    public final Asf4MemberRepository asf4MemberRepository;

    @Autowired
    public Asf4MemberService(Asf4MemberRepository asf4MemberRepository, RestTemplateBuilder builder) {
        this.asf4MemberRepository = asf4MemberRepository;
    }

    /**
     * テーブルのデータ一覧を返すメソッドです。
     *
     * @return List型でメンバ一覧を返します。
     */
    public List<Asf4Member> selectAll() {
        List<Asf4Member> asf4MemberList = asf4MemberRepository.findAll();
        return asf4MemberList;
    }
}
