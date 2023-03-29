package study.jpapractice2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.jpapractice2.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() throws Exception {
        Member member = Member.builder()
                        .userName("memberA")
                        .build();
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() throws Exception {
        Member member1 = Member.builder()
                .userName("member1")
                .build();
        Member member2 = Member.builder()
                .userName("member2")
                .build();

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // リスト照会検証
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // カウント検証
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 削除
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUserNameAndAgeGreaterThan() throws Exception {
        Member m1 = Member.builder()
                .userName("AAA")
                .age(10)
                .build();
        Member m2 = Member.builder()
                .userName("AAA")
                .age(20)
                .build();
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUserNameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUserName()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() throws Exception {
        Member m1 = Member.builder()
                .userName("AAA")
                .age(10)
                .build();
        Member m2 = Member.builder()
                .userName("AAA")
                .age(20)
                .build();
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUserName("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void paging() throws Exception {
        // given
        memberJpaRepository.save(
                Member.builder()
                        .userName("member1")
                        .age(10)
                .build()
        );
        memberJpaRepository.save(
                Member.builder()
                        .userName("member2")
                        .age(10)
                        .build()
        );
        memberJpaRepository.save(
                Member.builder()
                        .userName("member3")
                        .age(10)
                        .build()
        );
        memberJpaRepository.save(
                Member.builder()
                        .userName("member4")
                        .age(10)
                        .build()
        );
        memberJpaRepository.save(
                Member.builder()
                        .userName("member5")
                        .age(10)
                        .build()
        );

        int age = 10;
        int offset = 1;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }
}