package io.github.xiaoyureed.springbootdemos.freemarkerh2jps;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/15
 */
public interface StudentRepo extends JpaRepository<Student, Long> {
}
