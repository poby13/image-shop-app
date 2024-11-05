package kr.co.cofile.sbimgshop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // WebMvcConfigurer를 사용한 CORS 설정
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")  // 모든 출처 허용. 실제 운영환경에서는 구체적인 도메인을 지정하는 것이 좋습니다.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)  // true로 설정하면 allowedOrigins에 와일드카드 (*) 사용 불가
                        .maxAge(3600);  // 클라이언트가 pre-flight 요청의 결과를 캐시하는 시간(요청의 안정성 검사 시간)
            }
        };
    }

    // 다른 방법으로 CorsFilter를 사용한 CORS 설정이 있음.
}