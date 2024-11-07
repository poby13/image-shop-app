package kr.co.cofile.sbimgshop.common.auth.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull @Size(min = 3, max = 100)
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