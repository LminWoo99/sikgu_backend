package com.example.plantcouponservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponseDto {
    private Long couponNo;
    private Integer memberNo;
    private Integer discountPrice;
    private LocalDateTime regDate;

}
