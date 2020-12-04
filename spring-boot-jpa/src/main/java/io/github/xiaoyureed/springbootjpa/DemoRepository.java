package io.github.xiaoyureed.springbootjpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/26
 */
//@Repository // don't need
public interface DemoRepository extends JpaRepository<Demo, Long> {
}
