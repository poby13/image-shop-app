package kr.co.cofile.sbimgshop.common.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotNull
    @Size(min = 3, max = 50)
    private String userId;

    //@NotNull @Size(min = 3, max = 100)
    @NotBlank
    // 길이조건 8-16자
    // 필수 포함
    // -- 영문자 최소1개 포함 (?=.*[A-Za-z])
    // -- 숫자 최소1개 포함 (?=.*\\d)
    // -- 특수문자 최소1개 포함 (?=.*[!\"#$%&'();:?@\\[\\]\\^_{|}~\\\\])
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!\"#$%&'();:?@\\[\\]\\^_{|}~\\\\])[A-Za-z\\d!\"#$%&'();:?@\\[\\]\\^_{|}~\\\\]{8,16}$",
            message = "비밀번호는 8-16자의 영문 대/소문자, 숫자, 특수문자를 사용할 수 있습니다."
    )
    private String userPw;

    @NotNull @Size(min = 2, max = 100)
    private String userName;

    private String jobGroupCode;
    private String job;

    public MemberDTO toMemberDTO(PasswordEncoder passwordEncoder) {
        return MemberDTO.builder()
                .userId(this.userId)
                .userPw(passwordEncoder.encode(this.userPw))
                .userName(this.userName)
                .jobGroupCode(this.jobGroupCode)
                .job(this.job)
                .status("ACTIVE")
                .build();
    }
}