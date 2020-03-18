package io.github.xiaoyu.thymeleafdemo.repository;

import io.github.xiaoyu.thymeleafdemo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
