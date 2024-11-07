package kr.co.cofile.sbimgshop.common.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonResponse {
    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    @Builder
    public JsonResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static JsonResponse success(String message) {
        return JsonResponse.builder()
                .success(true)
                .message(message)
                .build();
    }

    public static JsonResponse success(String message, Object data) {
        return JsonResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static JsonResponse success(String message, Object data, LocalDateTime timestamp) {
        return JsonResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(timestamp)
                .build();
    }

    public static JsonResponse error(String message) {
        return JsonResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}