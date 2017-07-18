package com.elasticsearch.service;

import com.elasticsearch.entity.User;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-7-12.
 */
@Service
public class ElasticsearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private Client client;

    public boolean insertOrUpdateUser(List<User> users) {
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        for (User u : users) {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(u.getUuid()).withObject(u).build();
            queries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(queries);
        return true;
    }

    public String query(String para) {
        SearchRequestBuilder requestBuilder = client.prepareSearch("es_test")
                .setTypes("user")
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH) //设置查询类型
                .setFrom(0).setSize(20)//设置分页信息
                .addSort("registeTime", SortOrder.ASC)//按照时间降序
                .setExplain(true);//设置是否按查询匹配度排序


        //requestBuilder.setQuery(QueryBuilders.matchAllQuery());//来匹配全部文档
        //requestBuilder.setQuery(QueryBuilders.matchQuery("company", uuid));//匹配单个字段
        //requestBuilder.setQuery(QueryBuilders.multiMatchQuery(uuid, "company", "userName"));//多个字段匹配某一个值,搜索company中或userName中包含有uuid的文档（必须与uuid一致）
        //requestBuilder.setQuery(QueryBuilders.wildcardQuery("company", "*" + uuid + "*"));//模糊查询，?匹配单个字符，*匹配多个字符
        requestBuilder.setQuery(QueryBuilders.queryStringQuery(para).queryName("company"));
        //requestBuilder.setQuery(QueryBuilders.termQuery("company", uuid));
        //requestBuilder.setQuery(QueryBuilders.fuzzyQuery("company", "*" + uuid + "*"));

        //复合查询
        /*QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(uuid);
        queryString.queryName("company");
        QueryStringQueryBuilder queryString1 = QueryBuilders.queryStringQuery(uuid);
        queryString1.queryName("userName");
        requestBuilder.setQuery(QueryBuilders.boolQuery().must(queryString).must(queryString1));*/

        SearchResponse resp = requestBuilder.execute().actionGet();
        //SearchResponse resp = client.prepareSearch().setFrom(0).setSize(20000).execute().actionGet();// 获取全部
        SearchHit[] hits = resp.getHits().getHits();
        JSONArray ja = new JSONArray();
        for (SearchHit hit : hits) {
            ja.put(hit.getSource());
        }
        //resp.getHits().getTotalHits() 获取总数
        System.out.println("result total size = " + resp.getHits().getTotalHits() + ", current size = " + ja.length());
        return ja.toString();
    }

}
