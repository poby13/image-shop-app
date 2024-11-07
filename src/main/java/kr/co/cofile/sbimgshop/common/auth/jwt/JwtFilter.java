package kr.co.cofile.sbimgshop.common.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);

        try {
            // 토큰이 존재 && 유효한 토큰인가?
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 토큰에서 클레임 추출 후 추출한 클레임에서 권한정보를 추출하여 Authentication 객체를 생성
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                // SecurityContext에 Authentication 객체를 저장 -> 이제 이 요청은 인증된 요청이 됨
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다.", authentication.getName());
            }
        } catch (Exception e) {
            log.error("사용자 인증을 설정할 수 없음: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}