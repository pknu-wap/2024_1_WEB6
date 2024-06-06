package com.web6.server.repository;

import com.web6.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    Member findByLoginId(String loginId);
    Member findByNickname(String nickname);
    Member findByKakaoId(String kakaoId);

    Member findByAccessToken(String accessToken);
    Member findByRefreshToken(String refreshToken);


}
