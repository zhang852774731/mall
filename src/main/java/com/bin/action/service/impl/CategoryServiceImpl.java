package com.bin.action.service.impl;

import com.bin.action.common.ServerResponse;
import com.bin.action.dao.CategoryMapper;
import com.bin.action.pojo.Category;
import com.bin.action.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.SetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by zhangbin on 18/1/3.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0){
            return ServerResponse.createBySuccess("增加分类成功");
        }else{
            return ServerResponse.createByErrorMessage("增加分类失败");
        }
    }

    @Override
    public ServerResponse updateCategory(String categoryName, Integer id) {
        Category category = new Category();
        category.setId(id);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0){
            return ServerResponse.createBySuccess("修改分类成功");
        }else {
            return ServerResponse.createBySuccess("修改分类失败");
        }
    }

    @Override
    public ServerResponse selectSonCategoryByParentId(Integer parentId) {
        List<Category> categories = categoryMapper.selectCategoryByParentId(parentId);
        if (CollectionUtils.isEmpty(categories)){
            logger.info("子类型为空");
        }
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse selectChildrenCategoryByParentId(Integer parentId) {
        Set<Category> categories = Sets.newHashSet();
        selectChildren(categories,parentId);
        List<Category> categoryList = Lists.newArrayList();
        for (Category category : categories){
            categoryList.add(category);
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    private void selectChildren(Set<Category> categories,int parentId){
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        if (category != null){
            categories.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(parentId);
        for (Category categoryItem : categoryList){
            selectChildren(categories,categoryItem.getId());
        }
    }
}
