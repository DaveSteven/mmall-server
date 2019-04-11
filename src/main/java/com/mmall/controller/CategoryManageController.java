package com.mmall.controller;

import com.mmall.annotation.LoginRequired;
import com.mmall.common.ServerResponse;
import com.mmall.holder.UserHolder;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Resource
    private IUserService iUserService;

    @Resource
    private ICategoryService iCategoryService;

    /**
     * 添加分类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @LoginRequired
    @PostMapping("addCategory")
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        if (iUserService.checkAdminRole(UserHolder.get()).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 更新分类
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    @LoginRequired
    @PostMapping("setCategoryName")
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {
        if (iUserService.checkAdminRole(UserHolder.get()).isSuccess()) {
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取分类
     *
     * @param categoryId
     * @return
     */
    @LoginRequired
    @GetMapping("getCategory")
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        if (iUserService.checkAdminRole(UserHolder.get()).isSuccess()) {
            // 查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
}
