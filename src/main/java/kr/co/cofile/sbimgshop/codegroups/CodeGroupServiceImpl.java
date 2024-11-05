package kr.co.cofile.sbimgshop.codegroups;

import kr.co.cofile.sbimgshop.common.dto.PageDTO;
import kr.co.cofile.sbimgshop.common.exception.BusinessException;
import kr.co.cofile.sbimgshop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodeGroupServiceImpl implements CodeGroupService {

    private final CodeGroupMapper codeGroupMapper;


    @Override
    public void createCodeGroup(CreateCodeGroupRequest createCodeGroupRequest) {
        // TODO DuplicateKeyException
        try {
            codeGroupMapper.insert(createCodeGroupRequest);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_KEY, e);
        }
    }

    @Override
    public PageDTO<CodeGroupResponse> getCodeGroups(int page, int size) {
        int offset = (page - 1) * size;
        int totalElements = codeGroupMapper.countTotal();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        CodeGroupSearchCondition condition = CodeGroupSearchCondition.builder()
                //.groupCode("001")
                .build();

        List<CodeGroupDTO> codeGroupDTOS = codeGroupMapper.selectByCondition(condition, offset, size);

        // DTO를 Response로 변환
        List<CodeGroupResponse> responses = codeGroupDTOS.stream()
                .map(CodeGroupResponse::from)
                .collect(Collectors.toList());

        return PageDTO.<CodeGroupResponse>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .content(responses)
                .build();
    }

    @Override
    public PageDTO<CodeGroupResponse> getCodeGroups(String codeGroup, String codeName, int page, int size) {
        int offset = (page - 1) * size;
        int totalElements = codeGroupMapper.countTotal();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        CodeGroupSearchCondition condition = CodeGroupSearchCondition.builder()
                .groupCode(codeGroup)
                .groupName(codeName)
                .build();

        List<CodeGroupDTO> codeGroupDTOS = codeGroupMapper.selectByCondition(condition, offset, size);

        // DTO를 Response로 변환
        List<CodeGroupResponse> codeGroupResponses = codeGroupDTOS.stream()
                .map(CodeGroupResponse::from)
                .collect(Collectors.toList());

        return PageDTO.<CodeGroupResponse>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .content(codeGroupResponses)
                .build();
    }

    @Override
    public CodeGroupDTO getCodeGroup(String groupCode) {

        return codeGroupMapper.selectByGroupCode(groupCode).orElseThrow(() -> new BusinessException(ErrorCode.NO_DATA_FOUND));
    }
}
