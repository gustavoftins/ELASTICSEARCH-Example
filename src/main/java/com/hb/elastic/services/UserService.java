package com.hb.elastic.services;

import com.hb.elastic.models.User;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private RestHighLevelClient client;

    public void save(User user) throws IOException {

        Map<String, Object> json = new HashMap<>();

        json.put("email", user.getEmail());
        json.put("firstName", user.getFirstName());
        json.put("lastName", user.getLastName());

        IndexRequest indexRequest = new IndexRequest("user").id(UUID.randomUUID().toString()).source(json);

        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public List<Map<String, Object>> findById(String id) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", id));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = response.getHits().getHits();
        List<Map<String, Object>> user = new LinkedList<>();

        for (SearchHit hit : hits) {
            user.add(hit.getSourceAsMap());
        }
        return user;
    }
}
