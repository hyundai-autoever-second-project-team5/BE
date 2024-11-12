package com.example.BE.review;


import com.example.BE.movie.MovieEntity;
import com.example.BE.movie.MovieRepository;
import com.example.BE.review.dto.ReviewRequestDto;
import com.example.BE.user.UserEntity;
import com.example.BE.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public ReviewEntity createReview(int movieId, ReviewRequestDto reviewRequestDto, int userId) {
        // 영화 존재 여부 확인
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다."));

        // 사용자 존재 여부 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // ReviewEntity 객체 생성
        ReviewEntity review = ReviewEntity.builder()
                .rating(reviewRequestDto.getRating())  // 별점
                .description(reviewRequestDto.getDescription())  // 한줄평
                .content(reviewRequestDto.getContent())  // 상세내용
                .movie(movie)  // 영화
                .user(user)  // 사용자
                .createDate(LocalDateTime.now())  // 리뷰 작성일
                .build();

        // 리뷰 저장
        return reviewRepository.save(review);
    }
    public ReviewEntity updateReview(int reviewId, ReviewRequestDto reviewUpdateRequestDto) {
        // 리뷰 존재 여부 확인
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // 리뷰 정보 업데이트
        review.setRating(reviewUpdateRequestDto.getRating());
        review.setDescription(reviewUpdateRequestDto.getDescription());
        review.setContent(reviewUpdateRequestDto.getContent());

        // 변경된 리뷰 저장
        return reviewRepository.save(review);
    }

    public void deleteReview(int reviewId){

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        reviewRepository.delete(review);
    }

}
