package com.mmall.controller;

import com.mmall.annotation.LoginRequired;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.data.entity.User;
import com.mmall.holder.UserHolder;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.RegEx;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
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
            return iCategoryService.setCategoryName(categoryName, categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
