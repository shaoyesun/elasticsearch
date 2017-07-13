package com.elasticsearch.util.es_main;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * 该包用于本地main测试
 *
 * @ 参考:http://www.656463.com/article/vU7jYr.htm
 * @ 1.添加索引
 * @ 2.删除索引中指定类别的指定记录
 * @ 3.获取指定索引的指定类别的指定记录
 */
public class ElasticSearchTest {

    //创建客户端，所有的操作都由客户端开始，这个就好像是JDBC的Connection对象
    public static Client getClient() {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("192.168.8.53", 9300));
        return client;
    }

    //关闭
    public static void closeClient(Client client) {
        client.close();
    }

    //添加索引
    public static void addIndex() {
        Client client = getClient();
        String json = ESUtils.toJson(new LogModel());
        IndexResponse response = client.prepareIndex("secisland", "tweet")
                //必须为对象单独指定ID
                .setId("4")
                .setSource(json)
                .execute()
                .actionGet();
        System.out.println(response.getId());
        closeClient(client);
    }

    //删除索引中指定类别的指定记录
    public static void delIndex() {
        Client client = getClient();
        DeleteResponse response = client.prepareDelete("secisland", "tweet", "4")
                .execute().actionGet();
        System.out.println(response.getId());
        closeClient(client);
    }

    //获取指定索引的指定类别的指定记录
    public static void getIndex() {
        Client client = getClient();
        GetResponse response = client.prepareGet("secisland", "tweet", "1")
                .execute().actionGet();
        System.out.println(response.getSource());
        closeClient(client);
    }

    public static void addData() {
        String[] desc = new String[]{
                "玉屏风口服液",
                "清咽丸",
                "四消丸",
                "感冒清胶囊",
                "人参归脾丸",
                "人参健脾丸",
                "明目地黄丸",
                "小儿咳喘灵颗粒",
                "小儿化痰止咳冲剂",
                "双黄连",
                "六味地黄丸"
        };
        Client client = getClient();
        int j = 0;
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int i = 1; i < 10; i++) {
            LogModel l = new LogModel();
            l.setDesc(desc[j]);
            j++;
            String json = ESUtils.toJson(l);
            IndexRequestBuilder indexRequest = client.prepareIndex("secisland", "tweet")
                    //指定不重复的ID
                    .setSource(json).setId(String.valueOf(i));
            //添加到builder中
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            System.out.println(bulkResponse.buildFailureMessage());
        }
        closeClient(client);
    }

    public static void query() {
        Client client = getClient();
        QueryBuilder query = QueryBuilders.matchQuery("desc", "小儿颗粒");
        SearchResponse response = client.prepareSearch("secisland")
                .setTypes("tweet")
                //设置查询条件,
                .setQuery(query)
                .setFrom(0).setSize(60)
                .execute()
                .actionGet();
        /**
         * SearchHits是SearchHit的复数形式，表示这个是一个列表
         */
        SearchHits shs = response.getHits();
        for (SearchHit hit : shs) {
            System.out.println("分数(score):" + hit.getScore() + ", 业务描述(desc):" +
                    hit.getSource().get("desc"));
        }
        client.close();
    }

    public static void main(String[] args) {
        //addIndex();
        //delIndex();
        //getIndex();
        //addData();
        query();
    }
}
