package com.example.demo.dao;

import com.example.demo.entities.File;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class ElasticsearchFileDao implements FileDaoCustom {

    private RestHighLevelClient client;
    private FileDao fileDao;

    @Autowired
    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    @Autowired
    public void setClient(RestHighLevelClient client) {
        this.client = client;
    }


    public Iterable<File> findAll() {
        return fileDao.findAll();
    }

    public Map<String, Object> findAllByTagsIsContaining(List<String> tags, int page, int pageSize) {
        BoolQueryBuilder tagsMatchQuery = QueryBuilders.boolQuery();

        for (String tag : tags) {
            tagsMatchQuery.filter(QueryBuilders.termQuery("tags", tag));
        }

        return searchAndCountBasedOnQuery(tagsMatchQuery, page, pageSize);
    }

    public Optional<File> findById(String id) {
        return fileDao.findById(id);
    }

    public File save(File file) {
        return fileDao.save(file);
    }

    public void deleteById(String id) {
        fileDao.deleteById(id);
    }

    @Override
    public void assignTags(File file, List<String> tags) {
        Script addTagScript = new Script(ScriptType.INLINE,
                "painless",
                "params['tags'].removeIf(tag -> ctx._source.tags.contains(tag));" +
                        "ctx._source.tags.addAll(params['tags'])",
                Map.of("tags", tags));

        updateBasedOnScript(file, addTagScript);
    }

    @Override
    public void removeTags(File file, List<String> tags) {
        Script removeTagScript = new Script(ScriptType.INLINE,
                "painless",
                "if (!ctx._source.tags.containsAll(params['tags'])) throw new Exception();" +
                        "ctx._source.tags.removeAll(params['tags'])",
                Map.of("tags", tags));

        updateBasedOnScript(file, removeTagScript);
    }

    @Override
    public Map<String, Object> findBySearchQueryAndCount(String searchQuery, int page, int size) {
        QueryBuilder matchQuery = QueryBuilders.wildcardQuery("name", "*"+ searchQuery +"*")
                                                .caseInsensitive(true);

        return searchAndCountBasedOnQuery(matchQuery, page, size);
    }

    private UpdateResponse updateBasedOnScript(File file, Script script) {
        UpdateRequest updateRequest = new UpdateRequest(File.INDEX_NAME, file.getId());
        updateRequest.script(script);

        try {
            return client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Can't remove tags!");
        }
    }

    private Map<String, Object> searchAndCountBasedOnQuery(QueryBuilder query, int page, int pageSize) {
        SearchRequest searchRequest = new SearchRequest(File.INDEX_NAME);
        CountRequest countRequest = new CountRequest(File.INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(query)
                .fetchSource(new String[] {"name", "size", "tags"}, new String[] {})
                .from(page)
                .size(pageSize);

        searchRequest.source(searchSourceBuilder);
        countRequest.query(query);

        Map<String, Object> response = new HashMap<>();

        List<File> files = new ArrayList<>();
        int totalCount;

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

            totalCount = (int) countResponse.getCount();

            SearchHits hits = searchResponse.getHits();

            for (SearchHit hit : hits) {
                Map<String, Object> fields = hit.getSourceAsMap();
                File file = new File();

                file.setId(hit.getId());
                file.setName((String) fields.get("name"));
                file.setSize((Integer) fields.get("size"));
                file.setTags((List<String>) fields.get("tags"));

                files.add(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't search!");
        }

        response.put("page", files);
        response.put("total", totalCount);

        return response;

    }
}
