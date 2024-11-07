package kr.co.cofile.sbimgshop.common.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MemberResponse {
    private String userId;
    private String userName;
    private String jobGroupCode;
    private String job;
    private String status;
    private List<String> authorities;

    public static MemberResponse from(MemberDTO memberDTO) {
        return MemberResponse.builder()
                .userId(memberDTO.getUserId())
                .userName(memberDTO.getUserName())
                .jobGroupCode(memberDTO.getJobGroupCode())
                .job(memberDTO.getJob())
                .status(memberDTO.getStatus())
                .authorities(memberDTO.getAuthorities().stream()
                        .map(MemberAuthDTO::getAuth)
                        .collect(Collectors.toList()))
                .build();
    }
}