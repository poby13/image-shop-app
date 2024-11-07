package kr.co.cofile.sbimgshop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtSecretGeneratorTest {
    @Test
    void generateRandomJwtSecret() {
        // given
        byte[] randomBytes = new byte[64]; // 64바이트 = 512비트
        new java.security.SecureRandom().nextBytes(randomBytes);

        // when
        String encodedString = Base64.getEncoder().encodeToString(randomBytes);

        // then
        System.out.println("Random JWT Secret: " + encodedString);
        assertThat(encodedString).hasSize(88); // Base64 인코딩된 64바이트는 88자
    }
}
