package io.github.xiaoyu.h2jpademo.repository;

import io.github.xiaoyu.h2jpademo.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xiaoyu
 * @since 1.0
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * 自定义简单查询
     * findXXBy,readAXXBy,queryXXBy,countXXBy
     */
    Book findByName(String name);

    /**
     * 关键字And 、 Or
     */
    Book findByNameOrPrice(String name, Double price);

    /**
     * fuzzy query
     */
    List<Book> findAllByNameContains(String keyName);

    /**
     * 修改、删除、统计也是类似语法
     */
    Long deleteByName(String name);
    Long countByName(String name);

    /**
     * 自定义分页
     */
    Page<Book> findAllByName(String name, Pageable pageable);

    /**
     * sql 查询
     */
    // @Modifying
    // @Query("update tbl_book b set b.name = ?1 where id = ?2")
    // int modifyById(String name, Long id);

    // @Transactional
    // @Modifying
    // @Query("delete from tbl_book where id = ?1")
    // void deleteById(Long id);

    /**
     * 多表查询
     * 第一种是利用 Hibernate 的级联查询来实现，第二种是创建一个结果集的接口来接收连表查询后的结果
     */


}
