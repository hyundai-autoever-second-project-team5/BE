package com.example.BE.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewRequestDto {

    private BigDecimal rating;      // 별점
    private String description;     // 한줄평
    private String content;

}
