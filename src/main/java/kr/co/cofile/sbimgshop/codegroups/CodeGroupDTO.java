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
@ToString
public class CodeGroupDTO {
    @NotBlank
    @Size(min = 3, max = 3)
    private String groupCode;

    @NotBlank
    @Size(min = 3, max = 3)
    private String groupName;

    private String useYn = "Y";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
