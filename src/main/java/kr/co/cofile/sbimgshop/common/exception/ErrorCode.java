package kr.co.cofile.sbimgshop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common Errors (400)
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Invalid HTTP Method"),
    INVALID_TYPE_VALUE(400, "C004", "Invalid Type Value"),
    MISSING_REQUEST_PARAMETER(400, "C005", "Missing Request Parameter"),

    // Authentication & Authorization Errors (400, 401, 403)
    UNAUTHORIZED(401, "A001", "Unauthorized Access"),
    INVALID_TOKEN(401, "A002", "Invalid or Expired Token"),
    ACCESS_DENIED(403, "A003", "Access Denied"),
    INVALID_CREDENTIALS(401, "A004", "Invalid Username or Password"),
    TOKEN_EXPIRED(401, "A005", "Token Has Expired"),

    // Database & MyBatis Errors (500)
    DATABASE_ERROR(500, "D001", "Database Error Occurred"),
    DUPLICATE_KEY(400, "D002", "Duplicate Key Found"),
    DATA_INTEGRITY_VIOLATION(400, "D003", "Data Integrity Violation"),
    MYBATIS_MAPPING_ERROR(500, "D004", "MyBatis Mapping Error"),
    NO_DATA_FOUND (404, "D005", "Requested Data Not Found"),

    // File Upload Errors (400)
    FILE_SIZE_EXCEED(400, "F001", "File Size Exceeds Maximum Limit"),
    INVALID_FILE_TYPE(400, "F002", "Invalid File Type"),
    FILE_UPLOAD_ERROR(500, "F003", "File Upload Failed"),

    // Validation Errors (400)
    VALIDATION_ERROR(400, "V001", "Validation Error"),
    DATA_FORMAT_ERROR(400, "V002", "Data Format Error"),  // 이름 변경
    REQUIRED_VALUE_MISSING(400, "V003", "Required Value Missing"),  // 이름 변경

    // Business Logic Errors (400)
    BUSINESS_RULE_VIOLATION(400, "B001", "Business Rule Violation"),
    INSUFFICIENT_BALANCE(400, "B002", "Insufficient Balance"),
    DUPLICATE_TRANSACTION(400, "B003", "Duplicate Transaction"),

    // Server Errors (500)
    INTERNAL_SERVER_ERROR(500, "S001", "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "S002", "Service Temporarily Unavailable"),
    EXTERNAL_API_ERROR(500, "S003", "External API Call Failed"),

    // Page Parameter Errors (400)
    INVALID_PAGE_NUMBER(400, "P001", "Page number must be greater than or equal to 0"),
    INVALID_PAGE_SIZE(400, "P002", "Page size must be between 1 and 50"),

    // Resource Not Found Errors (404)
    RESOURCE_NOT_FOUND(404, "R001", "Requested Resource Not Found"),  // URL을 찾을 수 없음
    PATH_NOT_FOUND(404, "R002", "Requested Path Not Found");         // 경로를 찾을 수 없음

    private final int status;
    private final String code;
    private final String message;
}