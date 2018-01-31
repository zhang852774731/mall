package com.bin.action.service;

import com.bin.action.common.ServerResponse;

/**
 * Created by zhangbin on 18/1/3.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName,Integer parentId);
    ServerResponse updateCategory(String categoryName,Integer id);
    ServerResponse selectSonCategoryByParentId(Integer parentId);
    ServerResponse selectChildrenCategoryByParentId(Integer parentId);
}
