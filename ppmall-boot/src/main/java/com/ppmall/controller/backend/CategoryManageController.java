package com.ppmall.controller.backend;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.ICategoryService;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping(value = "/addCategory")
    @ResponseBody
    public ServerResponse<String> updateInformation(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId, String mainImage) {
        return iCategoryService.addCategory(parentId, categoryName, mainImage);
    }

    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategory(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.getCategory(parentId);
    }

    @RequestMapping(value = "/getCategoryChild", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategoryAndChildren(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.getCategoryAndChildren(parentId);
    }

    @RequestMapping(value = "/getCategoryParent", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategoryParent(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.getCategoryParent(parentId);
    }

    @RequestMapping(value = "/setCategoryName", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> setCategoryName(HttpSession session, int categoryId, String categoryName) {
        return iCategoryService.setCategoryName(categoryId, categoryName);
    }
    
    @RequestMapping(value = "/delCategory", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> deleteCategory(HttpSession session, int categoryId) {
        return iCategoryService.delCategory(categoryId);
    }
    
    @RequestMapping(value = "/setCategoryImage", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> setCategoryImage(HttpSession session, int categoryId, String mianImage) {
        return iCategoryService.delCategory(categoryId);
    }


}
