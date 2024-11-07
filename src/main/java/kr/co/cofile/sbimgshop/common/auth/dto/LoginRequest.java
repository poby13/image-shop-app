package kr.co.cofile.sbimgshop.common.auth.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull @Size(min = 3, max = 100)
    private String userPw;
}