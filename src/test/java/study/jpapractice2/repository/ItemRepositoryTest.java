package study.jpapractice2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.jpapractice2.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Test
    public void save() throws Exception {
        Item item = new Item("A");
        itemRepository.save(item);
    }
}