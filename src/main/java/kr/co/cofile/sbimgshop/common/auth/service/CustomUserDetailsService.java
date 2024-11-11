package kr.co.cofile.sbimgshop.common.auth.service;

import kr.co.cofile.sbimgshop.common.auth.dto.CustomUserDetails;
import kr.co.cofile.sbimgshop.common.auth.mapper.MemberMapper;
import kr.co.cofile.sbimgshop.common.exception.BusinessException;
import kr.co.cofile.sbimgshop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        return new CustomUserDetails(memberMapper.selectMemberByUserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND)));
    }
}