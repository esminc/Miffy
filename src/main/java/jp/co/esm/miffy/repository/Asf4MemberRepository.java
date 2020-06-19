package jp.co.esm.miffy.repository;


import jp.co.esm.miffy.entity.Asf4Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Asf4MemberRepository extends JpaRepository<Asf4Member, Integer> {

}
