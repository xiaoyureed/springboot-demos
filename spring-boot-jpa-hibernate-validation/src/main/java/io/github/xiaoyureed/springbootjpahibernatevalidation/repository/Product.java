package io.github.xiaoyureed.springbootjpahibernatevalidation.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@Data
@Entity
@Table(name = "tb_product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String productName;

    @Basic
    @Column(name = "price", nullable = false)
    private BigDecimal productPrice;

    @Basic
    @Column(name = "expire_duration", nullable = false)
    private Integer expireDuration;

    @Basic
    @Column(name = "expire_time", nullable = false)
    private LocalDate expireTime;

    @Basic
    @Column(name = "create_time", nullable = false)
    private LocalDate createTime;

    @Transient
    private Integer priceInt;

    public Product(String productName, BigDecimal productPrice, int expireDuration) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.expireDuration = expireDuration;
        this.expireTime = LocalDate.now().plusDays(expireDuration);
        this.createTime = LocalDate.now();
        this.priceInt = productPrice.intValue();
    }
}
