package kr.co.cofile.sbimgshop.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 메소드 내에서 발생하지 않는 예외들의 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Method Argument Type Mismatch Exception", e);

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundExceptionException(NoResourceFoundException e) {
        log.error("No static resource ...", e);

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Request Body가 Null");

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.MISSING_REQUEST_Body);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 비즈니스 예외처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("Business Exception", e);

        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CustomValidationException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(CustomValidationException e) {
        log.error("Validation Exception", e);

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = ErrorResponse.bindingResultToMap(bindingResult);

        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 다른 예외들도 함께 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error occurred: ", e);

        // 원인 예외
        Throwable cause = e.getCause();

        // BusinessException이 원인인 경우
        if (cause instanceof BusinessException) {
            BusinessException businessException = (BusinessException) cause;
            ErrorCode errorCode = businessException.getErrorCode();
            ErrorResponse response = ErrorResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .status(errorCode.getStatus())
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        ErrorResponse response = ErrorResponse.builder()
                .code("S001")
                .message("서버 오류가 발생했습니다.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}