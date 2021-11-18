package com.example.demo.services;

import com.example.demo.entities.File;

import java.util.List;
import java.util.Map;

public interface FileService {

    List<File> findAll();
    Map<String, Object> findByTagsUsingPage(List<String> tags, int page, int pageSize);
    void save(File file);
    void deleteById(String id);
    void assignTags(String id, List<String> tags);
    void removeTags(String id, List<String> tags);
    Map<String, Object> findBySearchQueryAndCount(String searchQuery, int page, int size);
    String resolveTag(String extension);
}
