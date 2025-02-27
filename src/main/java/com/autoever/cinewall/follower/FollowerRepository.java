package com.autoever.cinewall.follower;

import com.autoever.cinewall.follower.dto.response.FollowerResponseDto;
import com.autoever.cinewall.follower.dto.response.FollowingResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<FollowerEntity, Integer> {

    @Query("SELECT new com.autoever.cinewall.follower.dto.response.FollowingResponseDto(f.followerId, f.toUser.userId, f.toUser.nickname, f.toUser.profile_url) " +
            "FROM FollowerEntity f " +
            "WHERE f.fromUser.userId = :userId")
    List<FollowingResponseDto> findFollowingByUserId(@Param("userId") int userId);

    @Query("SELECT new com.autoever.cinewall.follower.dto.response.FollowerResponseDto(f.followerId, f.fromUser.userId, f.fromUser.nickname, f.fromUser.profile_url) " +
            "FROM FollowerEntity f " +
            "WHERE f.toUser.userId = :userId")
    List<FollowerResponseDto> findFollowerByUserId(@Param("userId") int userId);

    @Query("SELECT f FROM FollowerEntity f WHERE f.fromUser.userId = :fromUserId AND f.toUser.userId = :toUserId")
    Optional<FollowerEntity> getFollowerByFromUserIdAndToUserId(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);
}
