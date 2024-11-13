package com.example.BE.review.dto;

import com.example.BE.movie.MovieEntity;
import com.example.BE.review.ReviewEntity;
import com.example.BE.user.UserEntity;
import com.example.BE.user.dto.ResponseUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseReviewPoster {
    private int reviewId;
    private ResponseUserInfo user;
    private MovieEntity movie;
    private BigDecimal rating; // double 타입은 고정 소숫점이 안된다고 하여 BigDecimal 사용
    private String description;
    private String content;
    private java.time.LocalDateTime createDate;

    public ResponseReviewPoster(ReviewEntity review) {
        this.reviewId = review.getReviewId();
        this.user = new ResponseUserInfo(review.getUser());
        this.movie = review.getMovie();
        this.rating = review.getRating();
        this.description = review.getDescription();
        this.content = review.getContent();
        this.createDate = review.getCreateDate();
    }
}
