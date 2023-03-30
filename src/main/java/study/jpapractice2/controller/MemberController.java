package study.jpapractice2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.jpapractice2.dto.MemberDto;
import study.jpapractice2.entity.Member;
import study.jpapractice2.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUserName();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "userName") Pageable pageable) {
        return memberRepository.findAll(pageable).map(member -> MemberDto.builder()
                .id(member.getId())
                .userName(member.getUserName())
                .build());
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUserName();
    }

    //@PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(Member.builder()
                    .userName("user" + i)
                    .age(i)
                    .build());
        }
    }
}