package io.github.xiaoyureed.springbootdatarestreact;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * https://spring.io/guides/tutorials/react-and-spring-data-rest/
 *
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/10/28
 */
@SpringBootApplication
public class SpringBootDataRestReactApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataRestReactApp.class);
    }

    @Bean
    public CommandLineRunner dataInit(EmpRepo repo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Emp emp = new Emp();
                emp.setName("xy");
                emp.setSalary(new BigDecimal(15000));
                repo.save(emp);
            }
        };

    }
}

@Controller
class HomeController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}

@Entity
@Data
//@Table(name = "tbl_book")
class Emp {
    /**
     * TABLE：使用一个特定的数据库表格来保存主键。
     * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
     * IDENTITY：主键由数据库自动生成（主要是自动增长型）
     * AUTO：主键自动选择以上三个
     */
    @Id
    @GeneratedValue
    private Long id;
//    @Column(name = "name", nullable = false)
    private String name;
    private BigDecimal salary;

}

//@Repository // 不想暴露 rest, 用这个
@RepositoryRestResource
interface EmpRepo extends CrudRepository<Emp, Long> {// JpaRepository is also ok
//    /**
//     * 自定义简单查询
//     * findXXBy,readAXXBy,queryXXBy,countXXBy
//     */
//    Book findByName(String name);
//
//    /**
//     * 关键字And 、 Or
//     */
//    Book findByNameOrPrice(String name, Double price);
//
//    /**
//     * fuzzy query
//     */
//    List<Book> findAllByNameContains(String keyName);
//
//    /**
//     * 修改、删除、统计也是类似语法
//     */
//    Long deleteByName(String name);
//    Long countByName(String name);
//
//    /**
//     * 自定义分页
//     */
//    Page<Book> findAllByName(String name, Pageable pageable);
//
//    /**
//     * sql 查询
//     */
//    // @Modifying
//    // @Query("update tbl_book b set b.name = ?1 where id = ?2")
//    // int modifyById(String name, Long id);
//
//    // @Transactional
//    // @Modifying
//    // @Query("delete from tbl_book where id = ?1")
//    // void deleteById(Long id);
//
//    /**
//     * 多表查询
//     * 第一种是利用 Hibernate 的级联查询来实现，第二种是创建一个结果集的接口来接收连表查询后的结果
//     */

}
