package study.jpapractice2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpapractice2.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
