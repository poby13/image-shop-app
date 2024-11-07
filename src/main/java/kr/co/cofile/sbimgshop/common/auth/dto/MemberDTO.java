package kr.co.cofile.sbimgshop.common.auth.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long userNo;
    private String userId;
    private String userPw;
    private String userName;
    private String jobGroupCode;
    private String job;
    private Integer coin;
    private String status;
    private LocalDateTime lastLoginAt;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MemberAuthDTO> authorities;
}