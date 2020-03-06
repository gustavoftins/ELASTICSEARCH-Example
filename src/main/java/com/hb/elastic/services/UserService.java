package com.hb.elastic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hb.elastic.models.User;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public void save(User user) throws IOException {

        Map<String, Object> json = new HashMap<>();

        json.put("email", user.getEmail());
        json.put("firstName", user.getFirstName());
        json.put("lastName", user.getLastName());

        IndexRequest indexRequest = new IndexRequest("user").id(UUID.randomUUID().toString()).source(json);

        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public User findById(String id) throws IOException {
        GetRequest request = new GetRequest();
        request.index("user");
        request.id(id);

        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        Map<String, Object> result = response.getSource();

        if (result != null) {
            return objectMapper.convertValue(result, User.class);
        }
        throw new IllegalArgumentException("Usuário não encontrado");
    }

    public String update(String id, User user) throws IOException {
        User userTobeUpdated = this.findById(id);

        userTobeUpdated.setEmail(user.getEmail());
        userTobeUpdated.setFirstName(user.getFirstName());
        userTobeUpdated.setLastName(user.getLastName());

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("user");
        updateRequest.type("_doc");
        updateRequest.id(id);

        Map<String, Object> mapToRequest = objectMapper.convertValue(userTobeUpdated, Map.class);
        updateRequest.doc(mapToRequest);
        UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);

        return response.getResult().name();
    }

    public String deleteById(String id) throws IOException {
        DeleteRequest request = new DeleteRequest("user", "_doc", id);

        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        return response.getResult().name();
    }

    public List<User> findAll() throws IOException {
        SearchRequest request = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        SearchHit[] hits = response.getHits().getHits();

        List<User> users = new ArrayList<>();

        if (hits.length > 0) {
            return Arrays.stream(hits).map(hit -> objectMapper.convertValue(hit.getSourceAsMap(), User.class)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
