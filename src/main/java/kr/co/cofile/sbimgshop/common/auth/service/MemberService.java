package kr.co.cofile.sbimgshop.common.auth.service;

import kr.co.cofile.sbimgshop.common.auth.dto.MemberAuthDTO;
import kr.co.cofile.sbimgshop.common.auth.dto.MemberDTO;
import kr.co.cofile.sbimgshop.common.auth.mapper.MemberMapper;
import kr.co.cofile.sbimgshop.common.exception.BusinessException;
import kr.co.cofile.sbimgshop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;

    public boolean existsMemberByUserId(String userId) {
        return memberMapper.selectMemberByUserId(userId).orElse(null) != null;
    }

    @Transactional
    public void signup(MemberDTO member) {
        try {
            // 회원정보 추가
            memberMapper.createMember(member);
            // 회원권한 추가
            MemberAuthDTO memberAuth = MemberAuthDTO.builder()
                    .userNo(member.getUserNo())
                    .auth("ROLE_USER")
                    .build();
            memberMapper.createAuth(memberAuth);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }
    }

    @Transactional
    public void updateLastLogin(Long userNo) {
        memberMapper.updateLastLogin(userNo);
    }

    public Optional<MemberDTO> getMemberByUsername(String userName) {
        return memberMapper.selectMemberByUsername(userName);
    }
}