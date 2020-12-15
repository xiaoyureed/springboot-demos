package io.github.xiaoyureed.springbootjpahibernatevalidation.service;

import io.github.xiaoyureed.springbootjpahibernatevalidation.Resp;
import io.github.xiaoyureed.springbootjpahibernatevalidation.repository.IProductRepo;
import io.github.xiaoyureed.springbootjpahibernatevalidation.repository.Product;
import io.github.xiaoyureed.springbootjpahibernatevalidation.util.CopyUtils;
import io.github.xiaoyureed.springbootjpahibernatevalidation.vo.ProductCreateReq;
import io.github.xiaoyureed.springbootjpahibernatevalidation.vo.ProductCreateResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/12/5
 */
@Service
public class ProductService {
    @Autowired
    private IProductRepo productRepo;

    public Resp<ProductCreateResp> createProduct(ProductCreateReq req) throws Exception {
        Product exist = productRepo.findByProductName(req.getProductName());
        if (exist != null) {
            throw new Exception("product exist");
        }

        Product toInsert = CopyUtils.copyBean(req, new Product());
        Product newProduct = productRepo.save(toInsert);
        return Resp.ok(CopyUtils.copyBean(newProduct, new ProductCreateResp()));
    }
}
