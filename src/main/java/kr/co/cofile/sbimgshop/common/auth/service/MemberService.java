package kr.co.cofile.sbimgshop.common.auth.service;

import kr.co.cofile.sbimgshop.common.auth.dto.MemberAuthDTO;
import kr.co.cofile.sbimgshop.common.auth.dto.MemberDTO;
import kr.co.cofile.sbimgshop.common.auth.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;

    public boolean existsByUserId(String userId) {
        return memberMapper.selectByUserId(userId) != null;
    }

    @Transactional
    public void signup(MemberDTO member) {
        memberMapper.create(member);

        MemberAuthDTO memberAuth = MemberAuthDTO.builder()
                .userNo(member.getUserNo())
                .auth("ROLE_USER")
                .build();
        memberMapper.createAuth(memberAuth);
    }

    @Transactional
    public void updateLastLogin(Long userNo) {
        memberMapper.selectLastLogin(userNo);
    }

    public Optional<MemberDTO> getUserByUsername(String userName) {
        return memberMapper.selectByUsername(userName);
    }
}