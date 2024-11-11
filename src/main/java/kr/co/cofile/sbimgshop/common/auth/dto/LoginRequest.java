package kr.co.cofile.sbimgshop.common.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull
    @Size(min = 3, max = 50)
    private String userId;

    // @NotNull @Size(min = 3, max = 100)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!\"#$%&'();:?@\\[\\]\\^_{|}~\\\\])[A-Za-z\\d!\"#$%&'();:?@\\[\\]\\^_{|}~\\\\]{8,16}$",
            message = "비밀번호는 8-16자의 영문 대/소문자, 숫자, 특수문자를 사용할 수 있습니다."
    )
    private String userPw;
}