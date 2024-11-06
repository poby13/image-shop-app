package kr.co.cofile.sbimgshop.codegroups;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeGroupSearchCondition {
    private String groupCode;
    private String groupName;
    private String useYn;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String searchKeyword;
    private List<String> groupCodes;
    private String sortField;
    private String sortDirection;

    // 정렬 방향 enum
    public enum SortDirection {
        ASC, DESC
    }
}