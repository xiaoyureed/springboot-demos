package io.github.xiaoyureed.springbootjpahibernatevalidation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateReq {
    @NotBlank
    @Length(max = 2, message = "too long")
    private String productName;
    @NotNull
    private BigDecimal productPrice;
    @NotNull
    @Max(value = 5, message = "too large")
    private Integer expireDuration;
}
