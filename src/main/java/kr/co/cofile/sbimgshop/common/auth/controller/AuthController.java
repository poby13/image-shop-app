package kr.co.cofile.sbimgshop.common.auth.controller;

import jakarta.validation.Valid;
import kr.co.cofile.sbimgshop.common.auth.dto.CustomUserDetails;
import kr.co.cofile.sbimgshop.common.auth.jwt.JwtTokenProvider;
import kr.co.cofile.sbimgshop.common.auth.dto.*;
import kr.co.cofile.sbimgshop.common.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<JsonResponse> signup(@Valid @RequestBody SignupRequest request) {
        if (memberService.existsMemberByUserId(request.getUserId())) {
            throw new DuplicateKeyException("이미 사용중인 아이디입니다.");
        }

        MemberDTO member = request.toMemberDTO(passwordEncoder);
        memberService.signup(member);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonResponse.builder()
                        .success(true)
                        .message("회원가입이 완료되었습니다.")
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUserId(), request.getUserPw());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        MemberDTO member = userDetails.getMember();

        memberService.updateLastLogin(member.getUserNo());

        TokenResponse tokenResponse = TokenResponse.builder()
                .token(jwt)
                .userId(member.getUserId())
                .userName(member.getUserName())
                .build();

        return ResponseEntity.ok(JsonResponse.builder()
                .success(true)
                .message("로그인이 완료되었습니다.")
                .data(tokenResponse)
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<JsonResponse> getMyInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        MemberDTO memberDTO = memberService.getMemberByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(JsonResponse.builder()
                .success(true)
                .data(MemberResponse.from(memberDTO))
                .build());
    }

}