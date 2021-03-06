package com.wuit.dx.service.Impl;

import com.wuit.dx.dao.ProductImgDAO;
import com.wuit.dx.entity.Product;
import com.wuit.dx.entity.ProductImg;
import com.wuit.dx.service.ProductImgService;
import com.wuit.dx.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dx
 * @since 2019/3/12 16:45
 */
@Service
public class ProductImgServiceImpl implements ProductImgService {

    @Resource
    private ProductImgDAO productImgDAO;

    @Override
    public List<ProductImg> getProductImgByProduct(int productId) {
        return productImgDAO.findByProductId(productId);
    }

    @Override
    public ProductImg saveProductImg(ProductImg productImg) {
        return productImgDAO.save(productImg);
    }
}
