package com.example.plantsnsservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SnsPostRequestDto {
    private Long id;
    private String snsPostTitle;
    private String snsPostContent;
    private Long memberNo;
    //중복 방지
    private Set<String> hashTags;
    private LocalDateTime createdAt;

}
