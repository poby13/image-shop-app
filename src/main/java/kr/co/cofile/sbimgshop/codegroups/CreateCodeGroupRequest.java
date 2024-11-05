package kr.co.cofile.sbimgshop.codegroups;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCodeGroupRequest {
    @NotBlank
    @Size(min = 3, max = 3)
    private String groupCode;

    @NotBlank
    @Size(min = 3, max = 30)
    private String groupName;

    private String useYn = "Y";
}