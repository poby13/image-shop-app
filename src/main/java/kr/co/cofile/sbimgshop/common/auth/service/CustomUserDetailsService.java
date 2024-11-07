package kr.co.cofile.sbimgshop.common.auth.service;

import kr.co.cofile.sbimgshop.common.auth.CustomUserDetails;
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
        MemberDTO member = memberMapper.selectByUserId(userId);
        if (member == null) {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }
        return new CustomUserDetails(member);
    }
}