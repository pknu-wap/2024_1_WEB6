package com.web6.server.dto;

import com.web6.server.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MemberDetails implements UserDetails {

    private Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    /*
     * 계정의 role에 관해서
     * 아직 admin 계정과 일반 사용자 계정을 구분하자는 논의가 없었기에
     * 차후에 의논 후 Member테이블에 role필드 추가 예정
     * 추가하면 바로 아래 메서드에 복붙
     *
     * Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return member.getRole();
            }
        });

        return collection;
     *
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    public String getNickname() {
        return member.getNickname();
    }

    public String getAccessToken() { return member.getAccessToken(); }
    /*
    * 계정이 만료되었는지에 관한 메서드
    * 일단 그런 사항은 체크하지 않아서 값을 모드 true로 바꿈
    * 만약 활성화 시키고 싶다면, 테이블의 필드값을 추가해야 함
    */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
