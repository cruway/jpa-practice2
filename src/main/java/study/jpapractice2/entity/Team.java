package study.jpapractice2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "teamName"})
public class Team extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String teamName;
    @OneToMany(mappedBy = "team")
    private List<Member> members;

    @Builder
    public Team(Long id, String teamName) {
        this.id = id;
        this.teamName = teamName;
        this.members = new ArrayList<>();
    }
}
