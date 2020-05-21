package io.github.xiaoyureed.springbootdemos.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author xiaoyu
 * date: 2020/3/29
 */
@SpringBootApplication
public class MongoApp {
    public static void main(String[] args) {
    }
}

interface UserRepo extends MongoRepository<User, Long> {
}

@Service
class UserRepoCustomized {

    private final MongoTemplate template;

    @Autowired
    public UserRepoCustomized(MongoTemplate template) {
        this.template = template;
    }

    public long insert(User user) {
        User insert = template.insert(user);
        return insert.getId();
    }

    public long insertOrUpdate(User user) {
        // template.upsert(new Query())
        return 0L;
    }

    public void remove(long id) {
        template.remove(new Query(Criteria.where("id").is(id)), User.class);
    }

    public void update(User user) {

    }

}

/**
 * Serializable 不是必须
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// @Document // 不是必须
class User implements Serializable {
    private Long id;
    private String name;
    private String pwd;
}
