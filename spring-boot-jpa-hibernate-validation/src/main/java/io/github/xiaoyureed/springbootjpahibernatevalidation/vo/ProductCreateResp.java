package io.github.xiaoyureed.springbootjpahibernatevalidation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateResp {
    private Long id;
    private String productName;
    private BigDecimal productPrice;
    private Date expireTime;
    private Date createTime;
    private Integer priceInt;
}
