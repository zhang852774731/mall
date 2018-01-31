package com.bin.action.service;

import com.bin.action.common.ServerResponse;
import com.bin.action.pojo.Product;

/**
 * Created by zhangbin on 18/1/4.
 */
public interface IProductService {
    ServerResponse saveOrUpdate(Product product);
    ServerResponse setStatus(Integer productId,Integer status);
    ServerResponse getDetail(Integer productId);
    ServerResponse list(Integer pageNum,Integer pageSize);
    ServerResponse productSearch(String productName,Integer productId,Integer pageNum,Integer pageSize);
}
