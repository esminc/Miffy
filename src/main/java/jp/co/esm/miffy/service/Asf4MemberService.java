package jp.co.esm.miffy.service;

import jp.co.esm.miffy.entity.Asf4Member;
import jp.co.esm.miffy.repository.Asf4MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Asf4MemberService {
  public final Asf4MemberRepository asf4MemberRepository;

  public List<Asf4Member> selectAll() {
    List<Asf4Member> asf4MemberList = asf4MemberRepository.findAll();
    return asf4MemberList;
  }
}
