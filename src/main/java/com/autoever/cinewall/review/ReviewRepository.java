package com.autoever.cinewall.review;

import com.autoever.cinewall.movie.MovieEntity;
import com.autoever.cinewall.review.dto.response.ResponseUserReviewGraph;
import com.autoever.cinewall.review.dto.response.ResponseUserReviewList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    @Query("SELECT r FROM ReviewEntity r WHERE r.movie.movieId = :movieId ORDER BY SIZE(r.hearts) DESC, r.createDate DESC")
    List<ReviewEntity> findFavoriteReviewsByMovieId(@Param("movieId") int movieId);

    // 특정 movieId에 해당하는 리뷰를 최신순으로 조회
    @Query("SELECT r FROM ReviewEntity r WHERE r.movie.movieId = :movieId ORDER BY r.createDate DESC")
    List<ReviewEntity> findLatestReviewsByMovieId(@Param("movieId") int movieId);


    // 특정 영화의 모든 리뷰의 평점을 가져오는 쿼리
    @Query("SELECT r.rating FROM ReviewEntity r WHERE r.movie.movieId = :movieId")
    List<BigDecimal> findRatingsByMovieId(@Param("movieId") int movieId);

    @Query("SELECT r FROM ReviewEntity r ORDER BY r.createDate DESC")
    List<ReviewEntity> findAllByCreateDateDesc();

    @Query("SELECT r.rating, COUNT(r) FROM ReviewEntity r WHERE r.movie.movieId = :movieId GROUP BY r.rating")
    List<Object[]> findRatingDistributionByMovieId(@Param("movieId") int movieId);

    List<ReviewEntity> findByUserUserIdOrderByCreateDateDesc(int userId);

    @Query("SELECT COUNT(r) FROM ReviewEntity r " +
            "WHERE r.user.userId = :userId AND SIZE(r.hearts) >= 5")
    int countReviewsByUserWithAtLeastFiveHearts(@Param("userId") int userId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.user.userId = :userId ORDER BY r.createDate DESC")
    List<ReviewEntity> findByUserIdOrderByCreateDateDesc(@Param("userId") int userId);

    @Query(
            "SELECT new com.autoever.cinewall.review.dto.response.ResponseUserReviewGraph(" +
                    "CAST(SUM(CASE WHEN r.rating >= 0 AND r.rating <= 1 THEN 1 ELSE 0 END) AS int), " +
                    "CAST(SUM(CASE WHEN r.rating > 1 AND r.rating <= 2 THEN 1 ELSE 0 END) AS int), " +
                    "CAST(SUM(CASE WHEN r.rating > 2 AND r.rating <= 3 THEN 1 ELSE 0 END) AS int), " +
                    "CAST(SUM(CASE WHEN r.rating > 3 AND r.rating <= 4 THEN 1 ELSE 0 END) AS int), " +
                    "CAST(SUM(CASE WHEN r.rating > 4 AND r.rating <= 5 THEN 1 ELSE 0 END) AS int)) " +
                    "FROM ReviewEntity r WHERE r.user.userId = :userId"
    )
    ResponseUserReviewGraph getRatingCounts(@Param("userId") int userId);

    @Query("SELECT new com.autoever.cinewall.review.dto.response.ResponseUserReviewList(" +
            "r.reviewId, r.user, r.movie, r.rating, r.description, r.content, r.createDate," +
            "(SELECT COUNT(h) FROM ReviewHeartEntity h WHERE h.review = r), " +
            "CASE WHEN (SELECT COUNT(h) FROM ReviewHeartEntity h WHERE h.user = r.user AND h.review = r) > 0 THEN true ELSE false END) " +
            "FROM ReviewEntity r WHERE r.user.userId = :userId " +
            "ORDER BY r.createDate DESC ")
    List<ResponseUserReviewList> findUserReviewsByUserId(@Param("userId") int userId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.user.userId = :userId")
    List<ReviewEntity> findPosterByUserId(@Param("userId") int userId);

    @Query("SELECT r.movie " +
            "FROM ReviewEntity r WHERE r.user.userId = :userId " +
            "ORDER BY r.rating DESC ")
    List<MovieEntity> findTop10(@Param("userId") String userId, Pageable top10);

    @Query("SELECT r FROM ReviewEntity r WHERE r.user.userId = :userId AND r.rating >= 4")
    List<ReviewEntity> findByUserAndRatingGreaterThanEqual(@Param("userId") int userId);

    @Query("SELECT r " +
            "FROM ReviewEntity r " +
            "WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
    ReviewEntity findByUserIdAndMovieId(@Param("userId") int userId, @Param("movieId") int movieId);

    @Query("SELECT new com.autoever.cinewall.review.dto.response.ResponseUserReviewList(" +
            "r.reviewId, r.user, r.movie, r.rating, r.description, r.content, r.createDate," +
            "(SELECT COUNT(h) FROM ReviewHeartEntity h WHERE h.review = r), " +
            "CASE WHEN (SELECT COUNT(h) FROM ReviewHeartEntity h WHERE h.user = r.user AND h.review = r) > 0 THEN true ELSE false END) " +
            "FROM ReviewEntity r WHERE r.reviewId = :reviewId")
    ResponseUserReviewList findReviewDetail(@Param("reviewId") int reviewId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.reviewId = :reviewId")
    ReviewEntity findByReviewId(@Param("reviewId") int reviewId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE movie m
        JOIN (
            SELECT
                movie_id,
                COUNT(*) AS vote_count,
                AVG(rating) AS vote_average
            FROM review
            GROUP BY movie_id
        ) r ON m.movie_id = r.movie_id
        SET
            m.vote_count = r.vote_count,
            m.vote_average = r.vote_average
        """, nativeQuery = true)
    void updateMovieStats();

}