package com.example.demo.dao;

import com.example.demo.entities.File;

import java.util.List;
import java.util.Map;

public interface FileDaoCustom {

    void assignTags(File file, List<String> tags);
    void removeTags(File file, List<String> tags);
    Map<String, Object> findBySearchQueryAndCount(String searchQuery, int page, int size);
}
