package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCatController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(name = "id",defaultValue = "0") long parentId){
        List<EasyUITreeNode> contentCatList = contentCategoryService.getContentCatList(parentId);
        return contentCatList;
    }

    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public E3Result createContentCategory(long parentId,String name){
        E3Result result = contentCategoryService.addContentCategory(parentId, name);
        return result;
    }

    @RequestMapping(value = "/content/category/delete",method = RequestMethod.POST)
    @ResponseBody
    public void deleteContentCategory(long id){
        contentCategoryService.deleteContentCategory(id);

    }

    @RequestMapping(value = "/content/category/update",method = RequestMethod.POST)
    @ResponseBody
    public void updateContentCategory(long id,String name){
         contentCategoryService.updateContentCategory(id,name);

    }

}
