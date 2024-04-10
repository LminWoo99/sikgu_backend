package com.example.plantsnsservice.vo.response;

import com.example.plantsnsservice.domain.entity.SnsPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsPostResponseDto {
    private Long id;
    private String snsPostTitle;
    private String snsPostContent;
    private Long memberNo;
    private LocalDateTime createdAt;
    private Integer snsLikesCount;
    private Integer snsViewsCount;
    //이미 입력 요청에서 set로 중복 방지하므로 List 사용
    private List<String> hashTags;

}
