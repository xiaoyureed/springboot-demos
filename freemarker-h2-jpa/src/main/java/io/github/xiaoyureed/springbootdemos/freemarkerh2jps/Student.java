package io.github.xiaoyureed.springbootdemos.freemarkerh2jps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stu")
@DynamicInsert
@DynamicUpdate
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
//    @ColumnDefault("")
    private String name;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING) // 若不注解，枚举默认是次序数字
    private GenderEnum gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "money", nullable = false)
//    @ColumnTransformer(
//            read = "money / 100",
//            write = "? * 100"
//    )
    private Integer money;

    @Column(name = "friend_ids", nullable = false)
    private String friendIds;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Column(name = "updated_date", nullable = false)
    @UpdateTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedDate;

    @Transient
    private List<Student> friends;

    public Student(String name, GenderEnum gender, Integer age, LocalDate birth, Integer money, String friendIds) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.birth = birth;
        this.money = money;
        this.friendIds = friendIds;
    }
}
