package com.example.demo.controllers;

import com.example.demo.entities.File;
import com.example.demo.exceptions.WrongPathVariableException;
import com.example.demo.exceptions.WrongRequestBodyException;
import com.example.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    private final static Map<String, Object> SUCCESS_RESPONSE = Map.of("success", true);

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public Map<String, Object> register(@Valid @RequestBody File file) {
        fileService.save(file);

        if (file.getId() == null) {
            throw new RuntimeException("Something wrong with file saving!");
        }

        return Map.of("ID", file.getId());
    }

    @DeleteMapping("/{ID}")
    public Map<String, Object> delete(@PathVariable("ID") String id) {
        try {
            fileService.deleteById(id);
        }
        catch (IllegalArgumentException e) {
            throw new WrongPathVariableException(e.getMessage());
        }

        return SUCCESS_RESPONSE;
    }

    @PostMapping("{ID}/tags")
    public Map<String, Object> assignToFile(@PathVariable("ID") String id, @RequestBody List<String> tags) {
        try {
            fileService.assignTags(id, tags);
        }
        catch (IllegalArgumentException e) {
            throw new WrongPathVariableException(e.getMessage());
        }

        return SUCCESS_RESPONSE;
    }

    @DeleteMapping("{ID}/tags")
    public Map<String, Object> removeFromFile(@PathVariable("ID") String id, @RequestParam("tags") List<String> tags) {
        try {
            fileService.removeTags(id, tags);
        }
        catch (IllegalArgumentException e) {
            throw new WrongPathVariableException(e.getMessage());
        }
        catch (Exception e) {
            throw new WrongRequestBodyException("tag not found on file");
        }

        return SUCCESS_RESPONSE;
    }

    @GetMapping
    public Map<String, Object> list(@RequestParam(value = "tags", required = false) List<String> tags,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   @RequestParam(value = "q", required = false) String searchQuery) {

        if (tags != null) {
            return fileService.findByTagsUsingPage(tags, page, size);
        }

        if (searchQuery != null) {
            return fileService.findBySearchQueryAndCount(searchQuery, page, size);
        }

        List<File> filePage = fileService.findAll();

        return Map.of("total", filePage.size(), "page", filePage);
    }
}
