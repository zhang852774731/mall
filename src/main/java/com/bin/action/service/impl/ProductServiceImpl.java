package com.bin.action.service.impl;

import com.bin.action.common.ResponseCode;
import com.bin.action.common.ServerResponse;
import com.bin.action.dao.CategoryMapper;
import com.bin.action.dao.ProductMapper;
import com.bin.action.pojo.Category;
import com.bin.action.pojo.Product;
import com.bin.action.service.IProductService;
import com.bin.action.util.DateTimeUtil;
import com.bin.action.util.PropertiesUtil;
import com.bin.action.vo.ProductDetailVo;
import com.bin.action.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangbin on 18/1/4.
 */
@Service("iProduceService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdate(Product product) {
        if (product == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }else {
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] imgs = product.getSubImages().split(",");
                product.setMainImage(imgs[0]);
                if (product.getId() != null){
                    int rowCount = productMapper.updateByPrimaryKey(product);
                    if (rowCount > 0){
                        return ServerResponse.createBySuccess("商品更新成功");
                    }else {
                        return ServerResponse.createByErrorMessage("商品更新失败");
                    }
                }else {
                    int rowCount = productMapper.insert(product);
                    if (rowCount > 0){
                        return ServerResponse.createBySuccess("商品新增成功");
                    }else {
                        return ServerResponse.createByErrorMessage("商品新增失败");
                    }
                }
            }else {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
        }
    }

    @Override
    public ServerResponse setStatus(Integer productId, Integer status) {
        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }else{
            Product product = new Product();
            product.setId(productId);
            product.setStatus(status);
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0 ){
                return ServerResponse.createBySuccess("修改销售状态成功");
            }else {
                return ServerResponse.createByErrorMessage("修改销售状态失败");
            }
        }

    }

    @Override
    public ServerResponse getDetail(Integer productId) {
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("商品不存在");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse list(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.list();
        PageInfo pageInfo = new PageInfo(products);
        List<ProductListVo> productListVos = new ArrayList<>();
        for (Product product : products){
            ProductListVo productListVo = assembleroductListVo(product);
            productListVos.add(productListVo);
        }
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse productSearch(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.selectByProductNameAndProductId(productName,productId);
        PageInfo pageInfo = new PageInfo(products);
        List<ProductListVo> productListVos = new ArrayList<>();
        for (Product product : products){
            ProductListVo productListVo = assembleroductListVo(product);
            productListVos.add(productListVo);
        }
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.cloudmmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        productDetailVo.setParentCategoryId(category.getParentId());

        productDetailVo.setCreateTime(DateTimeUtil.date2Str(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.date2Str(product.getUpdateTime()));
        return productDetailVo;
    }

    private ProductListVo assembleroductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.cloudmmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }
}
