package kr.co.cofile.sbimgshop.common.auth.mapper;

import kr.co.cofile.sbimgshop.common.auth.dto.MemberAuthDTO;
import kr.co.cofile.sbimgshop.common.auth.dto.MemberDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    Optional<MemberDTO> selectMemberByUserId(String userId);

    @Insert("INSERT INTO member (user_id, user_pw, user_name, job_group_code, job, status) " +
            "VALUES (#{userId}, #{userPw}, #{userName}, #{jobGroupCode}, #{job}, 'ACTIVE')")
    @Options(useGeneratedKeys = true, keyProperty = "userNo")
    int createMember(MemberDTO member);

    @Insert("INSERT INTO member_auth (user_no, auth) VALUES (#{userNo}, #{auth})")
    int createAuth(MemberAuthDTO memberAuth);

    List<MemberAuthDTO> selectAuthoritiesByUserNo(Long userNo);

    @Update("UPDATE member SET last_login_at = CURRENT_TIMESTAMP WHERE user_no = #{userNo}")
    void updateLastLogin(Long userNo);

    Optional<MemberDTO> selectMemberByUsername(String userName);
}