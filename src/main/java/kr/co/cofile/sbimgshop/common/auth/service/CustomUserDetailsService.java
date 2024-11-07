package kr.co.cofile.sbimgshop.common.auth.service;

import kr.co.cofile.sbimgshop.common.auth.dto.CustomUserDetails;
import kr.co.cofile.sbimgshop.common.auth.mapper.MemberMapper;
import kr.co.cofile.sbimgshop.common.auth.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MemberDTO member = memberMapper.selectMemberByUserId(userId);
        if (member == null) {
            // TODO 사용자 예외처리
            throw new UsernameNotFoundException("사용자 아이디를 찾을 수 없음: " + userId);
        }
        return new CustomUserDetails(member);
    }
}