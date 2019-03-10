package cn.e3mall.search.dao;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索dao
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    /**
     *根据查询条件查询索引库
     * @param query
     * @return
     */
    public SearchResult search(SolrQuery query) throws SolrServerException {

        //根据query查询索引库
        QueryResponse queryResponse = solrServer.query(query);
        //取查询结果
        SolrDocumentList documents = queryResponse.getResults();
        //取查询结果总记录数
        long numFound = documents.getNumFound();
        SearchResult result = new SearchResult();
        result.setRecordCount(numFound);
        //取商品列表，显示高亮
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        List<SearchItem> itemList = new ArrayList<>();
        for (SolrDocument document:documents){
            SearchItem item = new SearchItem();
            item.setId((String) document.get("id"));
            item.setCategory_name((String) document.get("item_category_name"));
            item.setImage((String) document.get("item_image"));
            item.setPrice((long) document.get("item_price"));
            item.setSell_point((String) document.get("item_sell_point"));
            //取高亮显示
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            String title = "";
            if(list!=null&& list.size()>0){
                title = list.get(0);
            }else{
                title = (String) document.get("item_title");
            }
            item.setTitle(title);
            //添加到商品列表
            itemList.add(item);
        }

        result.setItemList(itemList);
        //返回结果

        return result;

    }

}
