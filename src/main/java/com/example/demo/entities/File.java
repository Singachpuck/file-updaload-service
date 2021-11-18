package com.example.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = File.INDEX_NAME)
public class File {

    public static final String INDEX_NAME = "files";

    @Id
    private String  id;
    @NotNull(message = "name has to be specified!")
    @Pattern(regexp = ".+\\.\\w+", message = "filename is invalid")
    private String name;
    @NotNull(message = "size has to be specified!")
    @Min(value = 0, message = "size has to be greater than 0")
    private Long size;
    private List<String> tags = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String resolveExtension() {
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
