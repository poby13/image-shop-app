package kr.co.cofile.sbimgshop.codegroups;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
public class CodeGroupDTO {
    private String groupCode;
    private String groupName;
    private String useYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Request -> DTO 변환
    public static CodeGroupDTO from(CreateCodeGroupRequest request) {
        CodeGroupDTO dto = new CodeGroupDTO();
        dto.setGroupCode(request.getGroupCode());
        dto.setGroupName(request.getGroupName());
        dto.setUseYn(request.getUseYn());
        return dto;
    }
}