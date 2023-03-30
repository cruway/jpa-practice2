package study.jpapractice2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @PersistenceContext
    EntityManager em;

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

    @Test
    public void paging() throws Exception {
        // given
        memberRepository.save(
                Member.builder()
                        .userName("member1")
                        .age(10)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member2")
                        .age(10)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member3")
                        .age(10)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member4")
                        .age(10)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member5")
                        .age(10)
                        .build()
        );

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // entity -> dto
        Page<MemberDto> toMap = page.map(m -> MemberDto.builder()
                .id(m.getId())
                .userName(m.getUserName())
                .teamName(null)
                .build());

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() throws Exception {
        // given
        memberRepository.save(
                Member.builder()
                        .userName("member1")
                        .age(10)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member2")
                        .age(19)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member3")
                        .age(20)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member4")
                        .age(21)
                        .build()
        );
        memberRepository.save(
                Member.builder()
                        .userName("member5")
                        .age(40)
                        .build()
        );

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        //em.flush();
        //em.clear();

        List<Member> result = memberRepository.findByUserName("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = Team.builder()
                .teamName("teamA")
                .build();
        Team teamB = Team.builder()
                .teamName("teamB")
                .build();
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = Member.builder()
                .userName("member1")
                .age(10)
                .team(teamA)
                .build();
        Member member2 = Member.builder()
                .userName("member2")
                .age(10)
                .team(teamB)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        //select Member
        List<Member> members = memberRepository.findEntityGraphByUserName("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUserName());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getTeamName());
        }

        //then
    }
}