package io.github.xiaoyu.dockercomposedemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByIp(String ip);
}
