package kr.co.cofile.sbimgshop.codegroups;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodeGroupSearchRequest {
    @Length(min = 3, max = 3, message = "그룹 코드는 3자여야 합니다")
    private String groupCode;

    @Length(min = 3, max = 50, message = "그룹명은 3-50자 사이여야 합니다")
    private String groupName;

    @Pattern(regexp = "^[YN]$", message = "사용여부는 Y 또는 N이어야 합니다")
    private String useYn;

    @Past(message = "시작일은 오늘 이전이어야 합니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Past(message = "종료일은 오늘 이전이어야 합니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @Pattern(regexp = "^[a-zA-Z0-9\\s_]*$", message = "검색어는 문자, 숫자, 스페이스, _만 가능합니다")
    private String searchKeyword;

    @Size(max = 50, message = "그룹 코드 리스트는 최대 50개까지 가능합니다")
    private List<String> groupCodes;

    @Pattern(regexp = "^(groupCode|groupName|useYn|createdAt|updatedAt)$",
            message = "정렬 필드는 groupCode, groupName, useYn, createdAt, updatedAt 중 하나여야 합니다")
    private String sortField;

    @Pattern(regexp = "^(ASC|DESC)$", message = "정렬 방향은 ASC 또는 DESC여야 합니다")
    private String sortDirection;

    @AssertTrue(message = "종료일은 시작일 이후여야 합니다")
    private boolean isEndDateValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }

    public enum SortDirection {
        ASC, DESC
    }
}