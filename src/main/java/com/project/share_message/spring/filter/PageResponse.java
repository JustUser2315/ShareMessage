package com.project.share_message.spring.filter;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PageResponse<T> {
    List<T> content;
    Metadata metadata;
    public static <T> PageResponse<T> createPageResponse(Page<T> page){
        var metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalElements());
        return new PageResponse<>(page.getContent(), metadata);
    }
    @Value
    public static class Metadata{
        int numberOfPage;
        int size;
        long totalElements;
    }
}
