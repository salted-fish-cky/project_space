package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 */
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {

        //从消息中取商品id
        TextMessage textMessage = (TextMessage) message;
        String text = null;
        try {
            text = textMessage.getText();
           Long itemId = new Long(text);
//           Thread.sleep(100);
           //根据商品id查询商品信息
            SearchItem item = itemMapper.getSearchItemById(itemId);
            //创建一个文档对象
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
            //提交

            System.out.println("添加索引成功");
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
