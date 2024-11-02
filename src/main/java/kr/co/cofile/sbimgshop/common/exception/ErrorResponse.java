package kr.co.cofile.sbimgshop.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;

    // 해당 필드가 null일 경우 JSON 직렬화 시 해당 필드는 제외시켜 응답 크기를 줄임
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<String, String> errors;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 유효성 검사의 여러개의 에러가 있는 경우
    public static ErrorResponse of(ErrorCode errorCode, Map<String, String> errors) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
