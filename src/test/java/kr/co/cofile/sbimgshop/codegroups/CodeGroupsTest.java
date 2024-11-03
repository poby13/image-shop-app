package kr.co.cofile.sbimgshop.codegroups;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

@SpringBootTest
@Slf4j
public class CodeGroupsTest {

    @Autowired
    private CodeGroupMapper codeGroupMapper;

    @Test
    void createCodeGroup() {
        int size = 49; // 추가 건수
        for (int i = 1; i <= size; i++) {
            CodeGroupDTO codeGroupDTO = new CodeGroupDTO();
            String groupCode = String.format("%03d", i);
            String groupName = generateGroupName(i);

            codeGroupDTO.setGroupCode(groupCode);
            codeGroupDTO.setGroupName(groupName);

            if (!codeGroupMapper.exists(groupCode)) {
                codeGroupMapper.create(codeGroupDTO);
                log.info("코드그룹: {}, 코드이름: {}", groupCode, groupName);
            } else {
                log.warn("코드그룹이 존재하여 스킵: {}", groupCode);
            }
        }
    }

    /**
     * 인덱스를 알파벳 조합으로 변환하는 메소드
     * 예: 1 -> "aaa", 2 -> "aab", ..., 26 -> "aaz", 27 -> "aba"
     */
    private String generateGroupName(int index) {
        // 0부터 시작하도록 조정
        index = index - 1;

        char[] result = new char[3];
        for (int i = 2; i >= 0; i--) {
            result[i] = (char) ('a' + (index % 26));
            index = index / 26;
        }
        return new String(result);
    }

    // 벌크 데이터 삭제를 위한 메소드 추가
    @Test
    void cleanupCodeGroups() {
        List<CodeGroupDTO> codeGroups = codeGroupMapper.findAll();
        for (CodeGroupDTO group : codeGroups) {
            codeGroupMapper.delete(group.getGroupCode());
        }
    }
}
