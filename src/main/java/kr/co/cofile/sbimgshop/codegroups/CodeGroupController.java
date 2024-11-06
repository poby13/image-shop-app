package kr.co.cofile.sbimgshop.codegroups;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import kr.co.cofile.sbimgshop.common.dto.PageDTO;
import kr.co.cofile.sbimgshop.common.exception.BusinessException;
import kr.co.cofile.sbimgshop.common.exception.CustomValidationException;
import kr.co.cofile.sbimgshop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/codegroups")
public class CodeGroupController {

    private final CodeGroupService codeGroupService;
    private final ObjectMapper objectMapper;

    /**
     * REST API 테스트 케이스:
     *
     * 1. 정상 케이스 테스트
     * POST /codegroups
     * Request Body:
     * {
     *   "groupCode": "ABC",
     *   "groupName": "테스트 그룹",
     *   "useYn": "Y"
     * }
     * - 성공 시 HTTP 200 (OK) 반환
     * - 생성된 CodeGroup 정보 반환
     * - useYn 기본값 "Y" 확인
     *
     * 2. 유효성 검사 실패 테스트
     * 2-1. groupCode 검증
     * - null 값 전송 시 @NotBlank 검증
     * - 빈 문자열 전송 시 @NotBlank 검증
     * - 3자리 미만 전송 시 @Size 검증 (예: "AB")
     * - 3자리 초과 전송 시 @Size 검증 (예: "ABCD")
     *
     * 2-2. groupName 검증
     * - null 값 전송 시 @NotBlank 검증
     * - 빈 문자열 전송 시 @NotBlank 검증
     * - 3자리 미만 전송 시 @Size 검증 (예: "AB")
     * - 30자리 초과 전송 시 @Size 검증
     *
     * 3. 중복 코드 테스트
     * - 이미 존재하는 groupCode로 생성 시도
     *
     * 4. 잘못된 요청 형식 테스트
     * - JSON 형식이 아닌 데이터 전송
     * - 필수 필드 누락
     *
     * 예상 실패 응답:
     * HTTP Status: 400 Bad Request
     * {
     *   "code": "VAL_001",
     *   "message": "유효성 검사 오류",
     *   "errors": [
     *     {
     *       "field": "groupCode",
     *       "message": "코드는 3자리여야 합니다"
     *     }
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<CodeGroupResponse> createCodeGroup(@Valid @RequestBody CreateCodeGroupRequest createCodeGroupRequest,
                                                             BindingResult bindingResult) {
        log.info("register: {}", createCodeGroupRequest);

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(ErrorCode.VALIDATION_ERROR, bindingResult);
        }

        codeGroupService.createCodeGroup(createCodeGroupRequest);

        CodeGroupDTO codeGroupDTO = codeGroupService.getCodeGroup(createCodeGroupRequest.getGroupCode());
        CodeGroupResponse response = CodeGroupResponse.from(codeGroupDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * REST API 테스트 케이스:
     * <p>
     * 1. 정상 케이스 테스트
     * GET /codegroups?page=1&size=10
     * - 성공 시 HTTP 200 (OK) 반환
     * - PageDTO 형태로 CodeGroup 목록 반환
     * - 페이지 정보(현재 페이지, 전체 페이지 등) 포함 확인
     * <p>
     * 2. 페이지 파라미터 테스트
     * - page 파라미터 누락 시 기본값 1 적용 확인
     * - size 파라미터 누락 시 기본값 10 적용 확인
     * <p>
     * 3. 유효성 검사 테스트
     * - page가 음수일 경우 처리
     * - size가 0이하일 경우 처리
     * - size가 최대 허용치 초과할 경우 처리
     * <p>
     * 4. 에러 케이스 테스트
     * - 잘못된 파라미터 형식 (문자열 입력 등)
     * - 존재하지 않는 페이지 요청
     *
     * @param page 요청할 페이지 번호 (기본값: 1)
     * @param size 페이지당 항목 수 (기본값: 10)
     * @return ResponseEntity<PageDTO < CodeGroupResponse>> 코드그룹 목록과 페이지 정보
     */
    @GetMapping
    public ResponseEntity<PageDTO<CodeGroupResponse>> findCodeGroups(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size) {

        try {
            // 페이지 파라미터 유효성 검사
            int validatedPage = page < 1 ? 1 : page;

            // 사이즈 파라미터 유효성 검사
            int validatedSize;
            if (size < 1) {
                validatedSize = 1;
            } else if (size > 50) {
                validatedSize = 50;
            } else {
                validatedSize = size;
            }

            PageDTO<CodeGroupResponse> pageDTO = codeGroupService.getCodeGroups(validatedPage, validatedSize);
            return ResponseEntity.ok(pageDTO);
        } catch (MethodArgumentTypeMismatchException e) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, e);
        } catch (NullPointerException e) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, e);
        }
    }

    /**
     * REST API 테스트 케이스:
     *
     * 1. 정상 케이스 테스트
     * GET /codegroups/search
     * 1-1. 모든 파라미터 입력
     * - URL: /codegroups/search?groupCode=ABC&groupName=테스트&page=1&size=10
     * - 성공 시 HTTP 200 (OK) 반환
     * - PageDTO 형태로 검색 결과 반환
     *
     * 1-2. 선택적 파라미터 테스트
     * - groupCode만 입력: /codegroups/search?groupCode=ABC
     * - groupName만 입력: /codegroups/search?groupName=테스트
     * - 페이징만 입력: /codegroups/search?page=2&size=20
     * - 파라미터 없음: /codegroups/search
     *
     * 2. 페이지네이션 테스트
     * 2-1. 기본값 테스트
     * - page 파라미터 누락 시 기본값 1 적용
     * - size 파라미터 누락 시 기본값 10 적용
     *
     * 2-2. 유효성 검사
     * - page가 1 미만일 때 → 1로 조정
     * - size가 1 미만일 때 → 1로 조정
     * - size가 50 초과일 때 → 50으로 조정
     *
     * 3. 검색 결과 테스트
     * 3-1. 검색 결과가 있는 경우
     * Response Body 예시:
     * {
     *   "content": [
     *     {
     *       "groupCode": "ABC",
     *       "groupName": "테스트그룹",
     *       "useYn": "Y"
     *     }
     *   ],
     *   "totalPages": 1,
     *   "totalElements": 1,
     *   "size": 10,
     *   "page": 1
     * }
     *
     * 3-2. 검색 결과가 없는 경우
     * - 빈 content 배열 반환
     * - totalElements: 0
     * - totalPages: 0
     *
     * 4. 에러 케이스 테스트
     * 4-1. 잘못된 타입의 파라미터 입력
     * - page에 문자 입력: /codegroups/search?page=abc
     * - size에 문자 입력: /codegroups/search?size=abc
     */
    // 검색
    @GetMapping("/search")
    public ResponseEntity<PageDTO<CodeGroupResponse>> findCodeGroups(@RequestParam(value = "groupCode", required = false) String groupCode,
                                                                     @RequestParam(value = "groupName", required = false) String groupName,
                                                                     @RequestParam(value = "useYn", required = false) String useYn,
                                                                     @RequestParam(name = "page", defaultValue = "1") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size) {

        try {
            // 페이지 파라미터 유효성 검사
            int validatedPage = page < 1 ? 1 : page;

            // 사이즈 파라미터 유효성 검사
            int validatedSize;
            if (size < 1) {
                validatedSize = 1;
            } else if (size > 50) {
                validatedSize = 50;
            } else {
                validatedSize = size;
            }

            PageDTO<CodeGroupResponse> pageDTO = codeGroupService.getCodeGroups(groupCode, groupName, useYn, validatedPage, validatedSize);
            return ResponseEntity.ok(pageDTO);
        } catch (MethodArgumentTypeMismatchException e) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, e);
        } catch (NullPointerException e) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, e);
        }

    }

    /**
     * REST API 테스트 케이스:
     *
     * 1. 정상 케이스 테스트
     * GET /codegroups/{groupCode}
     * - URL 예시: /codegroups/ABC
     * - 성공 시 HTTP 200 (OK) 반환
     * - CodeGroupResponse 형태로 단일 코드그룹 정보 반환
     *
     * 2. Path Variable 테스트
     * 2-1. 올바른 형식의 groupCode 입력
     * - 3자리 코드: /codegroups/ABC
     *
     * 2-2. 잘못된 형식의 groupCode 입력
     * - 빈 값: /codegroups/
     * - 3자리 미만: /codegroups/AB
     * - 3자리 초과: /codegroups/ABCD
     *
     * 3. 데이터 존재 여부 테스트
     * 3-1. 존재하는 groupCode 조회
     * Response Body 예시:
     * {
     *   "groupCode": "ABC",
     *   "groupName": "테스트그룹",
     *   "useYn": "Y",
     *   "createdAt": "2024-11-06T10:00:00",
     *   "modifiedAt": "2024-11-06T10:00:00"
     * }
     *
     * 3-2. 존재하지 않는 groupCode 조회
     * - HTTP Status: 404 Not Found
     * {
     *   "code": "NOT_FOUND",
     *   "message": "요청한 코드그룹을 찾을 수 없습니다."
     * }
     *
     * 4. 예외 처리 테스트
     * 4-1. 잘못된 요청
     * - 잘못된 URL 패턴: /codegroups//ABC (중복 슬래시)
     * - 허용되지 않는 문자 포함: /codegroups/A%20B
     *
     * 4-2. 시스템 에러
     * - 데이터베이스 연결 실패
     * - 서버 내부 오류
     */
    @GetMapping("/{groupCode}")
    public ResponseEntity<CodeGroupResponse> getCodeGroup(@PathVariable("groupCode") String groupCode) {
        return ResponseEntity.ok(CodeGroupResponse.from(codeGroupService.getCodeGroup(groupCode)));
    }

    /**
     * REST API 테스트 케이스:
     *
     * 1. 정상 케이스 테스트 (전체 업데이트)
     * PUT /codegroups/{groupCode}
     * 1-1. 모든 필드 포함된 전체 업데이트
     * Request Body:
     * {
     *   "groupName": "수정된그룹명",
     *   "useYn": "N"
     * }
     * - 성공 시 HTTP 200 (OK) 반환
     * - 업데이트된 CodeGroupDTO 반환
     *
     * 1-2. 부분 업데이트 (일부 필드가 null인 경우)
     * Request Body:
     * {
     *   "groupName": "수정된그룹명",
     *   "useYn": null
     * }
     * - null 값이 있는 경우 partialUpdateCodeGroup 메소드 호출
     * - null이 아닌 필드만 업데이트
     *
     * 2. Path Variable 테스트
     * 2-1. 올바른 groupCode 형식
     * - PUT /codegroups/ABC
     *
     * 2-2. 잘못된 groupCode 형식
     * - 존재하지 않는 코드: 404 Not Found
     * - 잘못된 형식의 코드: 400 Bad Request
     *
     * 3. Request Body 유효성 검사
     * 3-1. groupName 필드 검증
     * - null 허용
     * - 입력 시 @Size(min = 3, max = 30) 검증
     *
     * 3-2. useYn 필드 검증
     * - null 허용
     * - 입력 시 Y 또는 N만 허용
     *
     * 4. 에러 케이스 테스트
     * 4-1. 잘못된 요청 형식
     * - JSON 형식이 아닌 데이터
     * - 필드 타입 불일치
     *
     * 4-2. 비즈니스 로직 오류
     * - 존재하지 않는 그룹코드
     * - 삭제된 그룹코드
     *
     * 5. 요청 예시
     * # 전체 업데이트
     * curl -X PUT http://localhost:8080/codegroups/ABC \
     *   -H "Content-Type: application/json" \
     *   -d '{
     *     "groupName": "수정된그룹명",
     *     "useYn": "N"
     *   }'
     *
     * # 부분 업데이트
     * curl -X PUT http://localhost:8080/codegroups/ABC \
     *   -H "Content-Type: application/json" \
     *   -d '{
     *     "groupName": "수정된그룹명"
     *   }'
     *
     * 6. 응답 예시
     * 6-1. 성공 응답 (200 OK)
     * {
     *   "groupCode": "ABC",
     *   "groupName": "수정된그룹명",
     *   "useYn": "N",
     *   "modifiedAt": "2024-11-06T10:00:00"
     * }
     *
     * 6-2. 실패 응답
     * - 404 Not Found: 그룹코드 없음
     * {
     *   "code": "D005",
     *   "message": "No Data Found"
     * }
     *
     * - 400 Bad Request: 유효성 검사 실패
     * {
     *   "code": "V001",
     *   "message": "Validation Error",
     *   "errors": [
     *     {
     *       "field": "groupName",
     *       "message": "크기가 3에서 30 사이여야 합니다"
     *     }
     *   ]
     * }
     */
    // 전체 업데이트 (PUT)
    @PutMapping("/{groupCode}")
    public ResponseEntity<CodeGroupDTO> updateCodeGroup(@PathVariable String groupCode,
                                                        @RequestBody @Valid UpdateCodeGroupRequest updateCodeGroupRequest,
                                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(ErrorCode.VALIDATION_ERROR, bindingResult);
        }

        if (hasNullValues(updateCodeGroupRequest)) {
            Map<String, Object> nonNullFields = convertToNonNullMap(updateCodeGroupRequest);
            CodeGroupDTO updatedGroup = codeGroupService.partialUpdateCodeGroup(groupCode, nonNullFields);
            return ResponseEntity.ok(updatedGroup);
        }
        CodeGroupDTO updatedGroup = codeGroupService.updateCodeGroup(groupCode, updateCodeGroupRequest);
        return ResponseEntity.ok(updatedGroup);
    }

    // 부분 업데이트 (PATCH)
    @PatchMapping("/{groupCode}")
    public ResponseEntity<CodeGroupDTO> partialUpdateCodeGroup(@PathVariable String groupCode,
                                                               @RequestBody Map<String, Object> updates,
                                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(ErrorCode.VALIDATION_ERROR, bindingResult);
        }

        CodeGroupDTO updatedGroup = codeGroupService.partialUpdateCodeGroup(groupCode, updates);
        return ResponseEntity.ok(updatedGroup);
    }

    private boolean hasNullValues(UpdateCodeGroupRequest request) {
        return objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {
        }).containsValue(null);
    }

    private Map<String, Object> convertToNonNullMap(UpdateCodeGroupRequest request) {
        return objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {
        }).entrySet().stream().filter(entry -> entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
