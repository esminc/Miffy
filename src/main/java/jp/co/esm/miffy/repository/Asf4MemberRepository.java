package jp.co.esm.miffy.repository;


import jp.co.esm.miffy.entity.Asf4Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Asf4MemberRepository extends JpaRepository<Asf4Member, Integer> {

    Optional<Asf4Member> findByIdAndSkipFalse(int cleanerTodayId);
}
