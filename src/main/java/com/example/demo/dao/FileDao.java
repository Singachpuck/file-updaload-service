package com.example.demo.dao;

import com.example.demo.entities.File;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface FileDao extends ElasticsearchRepository<File, String> {

}
