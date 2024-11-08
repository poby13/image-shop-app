package kr.co.cofile.sbimgshop.common.auth.controller;

import jakarta.validation.Valid;
import kr.co.cofile.sbimgshop.common.auth.dto.*;
import kr.co.cofile.sbimgshop.common.auth.jwt.JwtTokenProvider;
import kr.co.cofile.sbimgshop.common.auth.service.MemberService;
import kr.co.cofile.sbimgshop.common.exception.CustomValidationException;
import kr.co.cofile.sbimgshop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    /*
     * 1. @Valid 관련
     * - request body가 null인 경우: HttpMessageNotReadableException
     * - 필수 필드 누락: MethodArgumentNotValidException
     * - 입력값 형식 검증 실패: MethodArgumentNotValidException
     *     - 이메일 형식
     *     - 비밀번호 복잡도
     *     - 사용자 ID 길이/형식
     *     - 전화번호 형식 등
     *
     * 2. 중복 검사 관련
     * - 사용자 ID 중복: DuplicateKeyException
     * - DB unique 제약조건 위반 가능성(이메일, 전화번호 등): DataIntegrityViolationException
     *
     * 3. 비밀번호 암호화 관련
     * - passwordEncoder 실패: IllegalArgumentException
     * - 메모리 부족: OutOfMemoryError
     *
     * 4. DB 저장 관련
     * - DB 연결 실패: DataAccessException
     * - 트랜잭션 실패: TransactionException
     * - DB 제약조건 위반: DataIntegrityViolationException
     *
     * 5. 기타
     * - 서버 내부 오류: RuntimeException
     * - 메모리 부족: OutOfMemoryError
     * - JSON 파싱 오류: HttpMessageNotReadableException
     */
    @PostMapping("/signup")
    public ResponseEntity<JsonResponse> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) {
        // 가입정보 오류에 대한 예외처리
        if (bindingResult.hasErrors()) {
            log.info(bindingResult.toString());
            throw new CustomValidationException(ErrorCode.VALIDATION_ERROR, bindingResult);
        }

        // 사용자 중복 예외처리
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