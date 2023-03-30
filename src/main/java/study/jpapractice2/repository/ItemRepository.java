package study.jpapractice2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpapractice2.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
