package study.jpapractice2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpapractice2.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
