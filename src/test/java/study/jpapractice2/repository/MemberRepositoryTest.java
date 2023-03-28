package study.jpapractice2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.jpapractice2.dto.MemberDto;
import study.jpapractice2.entity.Member;
import study.jpapractice2.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() throws Exception {
        Member member = Member.builder()
                .userName("memberA")
                .build();
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);
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
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUserNameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUserName()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findTop3HelloBy() throws Exception {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void nameQuery() throws Exception {
        Member m1 = Member.builder()
                .userName("AAA")
                .age(10)
                .build();
        Member m2 = Member.builder()
                .userName("AAA")
                .age(20)
                .build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUserName("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() throws Exception {
        Member m1 = Member.builder()
                .userName("AAA")
                .age(10)
                .build();
        Member m2 = Member.builder()
                .userName("AAA")
                .age(20)
                .build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void findUserNameList() throws Exception {
        Member m1 = Member.builder()
                .userName("AAA")
                .age(10)
                .build();
        Member m2 = Member.builder()
                .userName("BBB")
                .age(20)
                .build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUserNameList();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() throws Exception {
        Team team = Team.builder()
                .teamName("teamA")
                .build();
        teamRepository.save(team);
        
        Member member = Member.builder()
                .userName("AAA")
                .age(10)
                .team(team)
                .build();
        memberRepository.save(member);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    // 位置基盤パラメータバインディングは使わないこと
    // 必ず名前基盤バインディングを使う
    @Test
    public void findByNames() throws Exception {
        Member m1 = Member.builder()
                .userName("AAA")
                .age(10)
                .build();
        Member m2 = Member.builder()
                .userName("BBB")
                .age(20)
                .build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() throws Exception {
        Member m1 = Member.builder()
                .userName("AAA")
                .age(10)
                .build();
        Member m2 = Member.builder()
                .userName("BBB")
                .age(20)
                .build();
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa1 = memberRepository.findListByUserName("AAA"); // 値がない場合、nullではなくて空欄値を取得
        Member aaa2 = memberRepository.findMemberByUserName("AAA"); // これはnull
        Member aaa3 = memberRepository.findOptionalByUserName("AAA").get();
        assertThat(aaa1.get(0)).isEqualTo(aaa2).isEqualTo(aaa3);
    }
}