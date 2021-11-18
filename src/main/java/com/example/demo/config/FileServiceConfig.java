package com.example.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FileServiceConfig {

    @Bean
    @Qualifier("extensionTagMap")
    public Map<String, String> extensionTagMap() {
        Map<String, String> map = new HashMap<>();

        map.put("webm", "video");
        map.put("avi", "video");
        map.put("wmv", "video");
        map.put("mkv", "video");
        map.put("mp4", "video");

        map.put("wav", "audio");
        map.put("mp3", "audio");
        map.put("ogg", "audio");
        map.put("aac", "audio");

        map.put("doc", "document");
        map.put("docx", "document");
        map.put("txt", "document");
        map.put("html", "document");
        map.put("odt", "document");
        map.put("xls", "document");
        map.put("xlsx", "document");
        map.put("ods", "document");
        map.put("ppt", "document");
        map.put("pptx", "document");

        map.put("jpeg", "image");
        map.put("png", "image");
        map.put("gif", "image");
        map.put("bmp", "image");

        return map;
    }
}
