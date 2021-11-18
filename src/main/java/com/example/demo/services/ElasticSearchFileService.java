package com.example.demo.services;

import com.example.demo.dao.ElasticsearchFileDao;
import com.example.demo.entities.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ElasticSearchFileService implements FileService {

    @Qualifier("extensionTagMap")
    private Map<String, String> extensionTagMapping;

    private ElasticsearchFileDao fileDao;

    @Autowired
    public void setFileDao(ElasticsearchFileDao fileDao) {
        this.fileDao = fileDao;
    }

    @Autowired
    public void setExtensionTagMapping(Map<String, String> extensionTagMapping) {
        this.extensionTagMapping = extensionTagMapping;
    }

    @Override
    public List<File> findAll() {
        return StreamSupport.stream(fileDao.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> findByTagsUsingPage(List<String> tags, int page, int pageSize) {
        return fileDao.findAllByTagsIsContaining(tags, page, pageSize);
    }

    @Override
    public void save(File file) {
        String tag = resolveTag(file.resolveExtension());

        if (tag != null) {
            file.getTags().add(tag);
        }

        fileDao.save(file);
    }

    @Override
    public void deleteById(String id) {
        fileDao.findById(id).orElseThrow(() -> new IllegalArgumentException("file not found"));

        fileDao.deleteById(id);
    }

    @Override
    public void assignTags(String id, List<String> tags) {
        fileDao.assignTags(fileDao
                                .findById(id)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("File with such id doesn't exists")
                                ), tags);
    }

    @Override
    public void removeTags(String id, List<String> tags) {
        fileDao.removeTags(fileDao
                                .findById(id)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("File with such id doesn't exists")
                                ), tags);
    }

    @Override
    public Map<String, Object> findBySearchQueryAndCount(String searchQuery, int page, int size) {
        return fileDao.findBySearchQueryAndCount(searchQuery, page, size);
    }

    @Override
    public String resolveTag(String extension) {
        return extensionTagMapping.get(extension);
    }
}
