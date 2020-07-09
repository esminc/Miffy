package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
     * テーブルのデータ一覧を返す
     *
     * @return List型のメンバ一覧
     */
    public List<Asf4Member> selectAll() {
        List<Asf4Member> asf4MemberList = asf4MemberRepository.findAllByOrderByIdAsc();
        return asf4MemberList;
    }

    /**
     * 指定のidobataIDに完全一致するデータを返す
     *
     * @param idobataId
     * @return Optional型のメンバ情報
     * @throws NoSuchElementException 指定のIDに一致する情報がありません
     */
    public Asf4Member selectByidobataId(String idobataId) {
        Optional<Asf4Member> asf4MemberOptional = asf4MemberRepository.findByIdobataId(idobataId);
        if (asf4MemberOptional.isPresent()) {
            return asf4MemberOptional.get();
        } else {
            throw new NoSuchElementException("指定のIDに一致する情報がありません。");
        }
    }

    /**
     * メンバ情報をエンティティに保存してDBに反映する
     *
     * @param asf4Member
     */
    public void update(Asf4Member asf4Member) {
        asf4MemberRepository.saveAndFlush(asf4Member);
    }

    /**
     * メンバ情報を削除する
     * @param asf4Member
     */
    public void delete(Asf4Member asf4Member) {
        asf4MemberRepository.delete(asf4Member);
    }
}
