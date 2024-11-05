package kr.co.cofile.sbimgshop.codegroups;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCodeGroupRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    private String groupName;

    @Pattern(regexp = "^[YN]$", message = "useYn은 'Y' 또는 'N'만 가능합니다")
    private String useYn;
}