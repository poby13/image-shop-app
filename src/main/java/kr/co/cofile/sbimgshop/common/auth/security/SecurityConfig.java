package kr.co.cofile.sbimgshop.common.auth.security;

import kr.co.cofile.sbimgshop.common.auth.exception.JwtAccessDeniedHandler;
import kr.co.cofile.sbimgshop.common.auth.exception.JwtAuthenticationEntryPoint;
import kr.co.cofile.sbimgshop.common.auth.jwt.JwtFilter;
import kr.co.cofile.sbimgshop.common.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable()) // 시큐리티 제공 기본 로그인 페이지와 처리를 비활성화
                // JWT는 세션을 필요하지 않음
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/auth/signup"
                        ).permitAll()
                        .requestMatchers("/auth/me").authenticated()
                        .anyRequest().authenticated()
                )
                // 예외처리
                // 시프링 시큐리티 인증/인가 과정에서의 예외발생 처리
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 비인증 사용자가 권한이 요구되는 리소스 접근 - 401 Unauthorized
                        .accessDeniedHandler(jwtAccessDeniedHandler) // 인증 사용자가 접근하는 리소스에 권하없음 - 405 Forbidden
                )
                // UsernamePasswordAuthenticationFilter를 실행하기 전에 JwtFilter를 실행한다.
                .addFilterBefore(new JwtFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 스프링 시큐리티에서 실제로 인증을 처리하는 인터페이스
    // JWT 토큰 발급 전 사용자 인증에 사용
    // username/password 검증에 사용
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}