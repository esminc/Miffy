package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
        List<Asf4Member> asf4MemberList = asf4MemberRepository.findAllByOrderByIdAsc();
        return asf4MemberList;
    }

    public Optional<Asf4Member> selectByidobataId(String idobataId) {
        Optional<Asf4Member> asf4MemberOptional = asf4MemberRepository.findByIdobataId(idobataId);
        return asf4MemberOptional;
    }

    public void update(Asf4Member asf4Member) {
        asf4MemberRepository.saveAndFlush(asf4Member);
        }
}
