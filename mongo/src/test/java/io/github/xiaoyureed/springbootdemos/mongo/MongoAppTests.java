package io.github.xiaoyureed.springbootdemos.mongo;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.stream.Stream;

// import static org.springframework.data.mongodb.core.query.Query.*;

/**
 * @author xiaoyu
 * date: 2020/3/29
 */
@SpringBootTest
@Slf4j
public class MongoAppTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testMongo() {
        // collection name 默认是 class name
        // 无法重复插入
        // upsert(...) 没有则新增，有则更新
        Stream.of("1oo", "2jj").map(name -> new User(Long.parseLong(name.substring(0, 1)),
                name.substring(1), name.substring(1) + "_pwd")).forEach(mongoTemplate::insert);

        User userById = mongoTemplate.findById(1L, User.class);
        log.info(">>> user (id = 1) : {}" , userById);

        List<User> usersFromDb = mongoTemplate.findAll(User.class);
        System.out.println(">>> user list");
        usersFromDb.forEach(System.out::println);

        Query query1 = new Query(Criteria.where("name").is("jj"));
        System.out.println(">>> user with query: " + mongoTemplate.findOne(query1, User.class));

        //更新查询返回结果集的第一条
        UpdateResult updateResult = mongoTemplate.updateFirst(new Query(Criteria
                .where("name").is("oo")), new Update().set("pwd", "new_pwd"), User.class);
        System.out.println("user update: " + mongoTemplate.findOne(new Query(Criteria
                .where("name").is("oo")), User.class));

    }

    /**
     * https://www.cnblogs.com/yanduanduan/p/10578254.html
     */
    @Test
    public void testPageQuery() {
        Stream.of("3aa", "4bb", "5cc", "6dd", "7ff", "9gg").map(name -> new User(
                Long.parseLong(name.substring(0, 1)), name.substring(1),
                name.substring(1) + "_pwd")).forEach(mongoTemplate::insert);
        // mongoTemplate.find(new Query().with())
    }
}
