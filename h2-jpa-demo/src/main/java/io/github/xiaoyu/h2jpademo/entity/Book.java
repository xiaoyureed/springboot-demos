package io.github.xiaoyu.h2jpademo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Data
@Entity
@Table(name = "tbl_book")
public class Book {
    /**
     * TABLE：使用一个特定的数据库表格来保存主键。
     * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
     * IDENTITY：主键由数据库自动生成（主要是自动增长型）
     * AUTO：主键自动选择以上三个
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "user_id")
    private Long userId;
}
