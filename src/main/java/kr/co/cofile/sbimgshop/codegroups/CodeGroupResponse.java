package kr.co.cofile.sbimgshop.codegroups;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CodeGroupResponse {
    private String groupCode;
    private String groupName;
    private String useYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    // DTO -> Response 변환
    public static CodeGroupResponse from(CodeGroupDTO dto) {
        CodeGroupResponse response = new CodeGroupResponse();
        response.groupCode = dto.getGroupCode();
        response.groupName = dto.getGroupName();
        response.useYn = dto.getUseYn();
        response.createdAt = dto.getCreatedAt();
        response.updatedAt = dto.getUpdatedAt();
        return response;
    }
}