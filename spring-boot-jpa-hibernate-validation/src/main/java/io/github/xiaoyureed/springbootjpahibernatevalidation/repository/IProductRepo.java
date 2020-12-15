package io.github.xiaoyureed.springbootjpahibernatevalidation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
public interface IProductRepo extends JpaRepository<Product, Long> {
    Product findByProductName(String productName);
}
