package kr.co.cofile.sbimgshop.common.auth.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthDTO {
    private Long authNo;
    private Long userNo;
    private String auth;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}