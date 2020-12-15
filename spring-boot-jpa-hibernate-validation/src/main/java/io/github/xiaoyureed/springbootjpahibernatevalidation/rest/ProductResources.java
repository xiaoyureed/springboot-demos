package io.github.xiaoyureed.springbootjpahibernatevalidation.rest;

import io.github.xiaoyureed.springbootjpahibernatevalidation.Resp;
import io.github.xiaoyureed.springbootjpahibernatevalidation.service.ProductService;
import io.github.xiaoyureed.springbootjpahibernatevalidation.vo.ProductCreateReq;
import io.github.xiaoyureed.springbootjpahibernatevalidation.vo.ProductCreateResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@RestController
public class ProductResources {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Resp<ProductCreateResp>> create(@Validated @RequestBody ProductCreateReq req)
            throws Exception {
        return ResponseEntity.ok(productService.createProduct(req));
    }
}
