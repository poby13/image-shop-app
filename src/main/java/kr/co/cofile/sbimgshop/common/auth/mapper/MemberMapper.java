package kr.co.cofile.sbimgshop.common.auth.mapper;

import kr.co.cofile.sbimgshop.common.auth.dto.MemberAuthDTO;
import kr.co.cofile.sbimgshop.common.auth.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    MemberDTO selectByUserId(String userId);
    void create(MemberDTO member);
    void createAuth(MemberAuthDTO memberAuth);
    List<MemberAuthDTO> selectAuthoritiesByUserNo(Long userNo);
    void selectLastLogin(Long userNo);
    Optional<MemberDTO> selectByUsername(String userName);
}