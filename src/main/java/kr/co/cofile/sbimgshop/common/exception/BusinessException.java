package kr.co.cofile.sbimgshop.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // 기본 메시지 사용
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 사용자 정의 메시지 사용
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // Throwable cause: Java의 예외 처리에서 예외의 원인을 나타내는 매개변수
    // 기본 메시지 사용 - 예외 원인과 함께
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    // 사용자 정의 메시지 사용 - 예외 원인과 함께
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
