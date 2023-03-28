package study.jpapractice2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpapractice2.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    List<Member> findTop3HelloBy();
}
