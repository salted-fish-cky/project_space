package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolrJ {

    @Test
    public void addDocument() throws IOException, SolrServerException {
        //创建一个SolrServer对象，创建一个链接。参数solr服务的url
        HttpSolrServer httpSolrServer = new HttpSolrServer("http://number3:8080/solr/collection1");
        //创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档对象中添加域。文档中必须包含一个id域
        document.addField("id","doc01");
        document.addField("item_title","测试商品01");
        document.addField("item_price",1000);
        //把文档写入索引库
        httpSolrServer.add(document);
        //提交
        httpSolrServer.commit();
    }

    @Test
    public void deleteDocument() throws IOException, SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer("http://number3:8080/solr/collection1");

        //删除文档
//        httpSolrServer.deleteById("doc01");
        httpSolrServer.deleteByQuery("id:1031886");
        //提交
        httpSolrServer.commit();
    }
}
