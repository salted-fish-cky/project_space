package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService{

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;
    @Override
    public E3Result importAllItms() {

        try {

            //查询商品列表
            List<SearchItem> itemList = itemMapper.getItemList();
            //遍历商品列表
            for (SearchItem item:itemList){
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档对象中添加域
                document.addField("id",item.getId());
                document.addField("item_title",item.getTitle());
                document.addField("item_sell_point",item.getSell_point());
                document.addField("item_price",item.getPrice());
                document.addField("item_image",item.getImage());
                document.addField("item_category_name",item.getCategory_name());

                //把文档对象写入索引库
                solrServer.add(document);

            }

            //提交
            solrServer.commit();
            //返回导入成功
            return E3Result.ok();
        }catch (Exception e){

            e.printStackTrace();
            return  E3Result.build(500,"数据导入时发生异常");
        }


    }
}
