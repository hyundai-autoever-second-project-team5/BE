package com.example.BE.follower.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FollowingResponseDto {
    int followerId;
    int user_id;
    String nickname;
    String profile_url;
}
