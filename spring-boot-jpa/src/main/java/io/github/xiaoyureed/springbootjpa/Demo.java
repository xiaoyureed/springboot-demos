package io.github.xiaoyureed.springbootjpa;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/25
 */
@Data
@Entity
public class Demo {
//    https://blog.csdn.net/u010596392/article/details/9174049
    @Id // primary key
//   GeneratedValue has two field:
//    - generator: primary key generator name, you can find from db
//    - strategy: primary key generated strategy: ( field's type should be short/long/int)
//          AUTO、auto specify (IDENTITY、SEQUENCE、TABLE)
//          IDENTITY、 for mysql/sqlserver, omit generator
//          SEQUENCE、for oracle, should use like this:
//              @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
//              @SequenceGenerator(name = "seq", sequenceName = "db_seq_stu", initialValue = 3, allocationSize = 10)
//          TABLE: generate  a table in db
//                @GeneratedValue(strategy = GenerationType.TABLE, generator = "tab")
//                @TableGenerator(name = "tab", table = "tab_student", allocationSize = 10,
//                    pkColumnName = "pk_col", valueColumnName = "val_col", pkColumnValue = "pk"
//                )
//
//    使用 UUID 作为主键
//    @Id
//    @GenericGenerator(name = "generator", strategy = "uuid")
//    @GeneratedValue(generator = "generator", strategy = GenerationType.TABLE)
//    @Column(name="id",length=32)
//    private String id;
//
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private BigDecimal salary;

    @Transient // 创建 dbtable 忽略 这个 field, 但是 ddl-auto: validate 还是会异常
    private String ext;
}
