package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

    TbItem getItemById(long itemId);

    EasyUIDataGridResult getItemList(int page,int rows);

    E3Result addItem(TbItem item,String desc);

    E3Result deleteItem(String ids);

    E3Result instockItem(String ids);

    E3Result reshelfItem(String ids);

    E3Result queryItemDesc(long id);

    E3Result updateItem(TbItem tbItem,String desc);

    TbItemDesc getItemDescById(long itemId);



}
