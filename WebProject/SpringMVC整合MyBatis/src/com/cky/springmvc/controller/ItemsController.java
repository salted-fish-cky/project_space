package com.cky.springmvc.controller;

import com.cky.springmvc.pojo.Items;
import com.cky.springmvc.pojo.QueryVo;
import com.cky.springmvc.service.ItemsService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class ItemsController {


    @Autowired
    private ItemsService itemsService;

    @RequestMapping(value = "/item/itemlist.action")
    public ModelAndView itemsList(){
        ModelAndView modelAndView = new ModelAndView();

        List<Items> itemsList = itemsService.findItemsList();
        modelAndView.addObject("itemList",itemsList);
        modelAndView.setViewName("itemList");

        return modelAndView;
    }

    //修改页面
    @RequestMapping(value = "/item/itemEdit.action")
    public ModelAndView toEdit(Integer id ,HttpServletRequest request, HttpServletResponse response, Model model){

//        String idStr = request.getParameter("id");
//        int id = Integer.parseInt(idStr);
        Items items = itemsService.findItemsById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("item",items);
        modelAndView.setViewName("editItem");

        return modelAndView;

    }

    //绑定pojo，提交修改页面入参，上传图片
    @RequestMapping(value = "/item/updateItem.action")
    public String updateItem(QueryVo vo,MultipartFile pictureFile) throws IOException {

        //保存图片到G:upload
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        //jpg
        String extension = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
        pictureFile.transferTo(new File("G:\\upload\\"+name+"."+extension));

        vo.getItems().setPic(name+"."+extension);

        itemsService.updateById(vo.getItems());

//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("success");

        return "redirect:/item/itemlist.action";
    }

}
