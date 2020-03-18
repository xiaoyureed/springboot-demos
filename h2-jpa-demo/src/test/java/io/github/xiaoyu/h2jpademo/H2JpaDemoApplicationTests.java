package io.github.xiaoyu.h2jpademo;

import io.github.xiaoyu.h2jpademo.entity.Book;
import io.github.xiaoyu.h2jpademo.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class H2JpaDemoApplicationTests {

    //@Test
    //public void contextLoads() {
    //}

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testBookJpaUpdate() {
        Book save = new Book();
        save.setName("book3");
        save.setPrice(28.01);
        bookRepository.save(save);
        assertThat(bookRepository.findAll()).hasSize(3);

        bookRepository.delete(save);
        assertThat(bookRepository.findAll()).hasSize(2);
        bookRepository.deleteById(1L);
        assertThat(bookRepository.findAll()).hasSize(1);

        long count = bookRepository.count();
        assertThat(count).isEqualTo(1L);

        boolean existsById = bookRepository.existsById(2L);
        assertThat(existsById).isEqualTo(true);

        Book book2 = new Book();
        book2.setName("book2");
        boolean exists = bookRepository.exists(Example.of(book2));
        assertThat(exists).isEqualTo(true);

        Book book21 = bookRepository.findByName("book2");
        assertThat(book21.getName()).isEqualTo("book2");

        Book book3 = new Book();
        book3.setName("book3");
        book3.setPrice(13.11);
        bookRepository.save(book3);
        List<Book> bookList1 = bookRepository.findAllByNameContains("book");
        assertThat(bookList1).hasSize(2);

        // Page<Book> bookPage = bookRepository.findAll(PageRequest.of(1, 2, new Sort(Sort.Direction.DESC, "id")));
        // Stream<Book> bookStream = bookPage.get();
        // Book book4 = bookStream.findFirst().get();
        // assertThat(book4.getName()).isEqualTo("book3");
    }

    @Test
    public void testBookJpaQuery() {
        List<Book> bookList = bookRepository.findAll();
        log.info(">>> bookList: {}", bookList);
        assertThat(bookList).hasSize(2);

        // primary query
        bookRepository.findById(1L)
                .ifPresent(book -> assertThat(book.getName()).isEqualTo("book1"));

        // conditional query
        Book queryParams = new Book();
        queryParams.setName("book2");
        bookRepository.findOne(Example.of(queryParams))
                .ifPresent(book -> assertThat(book.getId()).isEqualTo(2L));
    }

}
