package kr.co.cofile.sbimgshop.codegroups;

import kr.co.cofile.sbimgshop.common.dto.PageDTO;
import kr.co.cofile.sbimgshop.common.exception.BusinessException;
import kr.co.cofile.sbimgshop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeGroupServiceImpl implements CodeGroupService {

    private final CodeGroupMapper codeGroupMapper;


    @Override
    public void createCodeGroup(CodeGroupDTO codeGroupDTO) {
        // TODO DuplicateKeyException
        try {
            codeGroupMapper.insert(codeGroupDTO);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ErrorCode.DUPLICATE_KEY, e);
        }
    }

    @Override
    public PageDTO getCodeGroups(int page, int size) {
        int offset = (page - 1) * size;
        int totalElements = codeGroupMapper.countTotal();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        CodeGroupSearchCondition condition = CodeGroupSearchCondition.builder()
                //.groupCode("001")
                .build();

        List<CodeGroupDTO> codeGroupDTOS = codeGroupMapper.seletByCondition(condition, offset, size);

        return PageDTO.<CodeGroupDTO>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .content(codeGroupDTOS)
                .build();
    }

    @Override
    public PageDTO getCodeGroups(String codeGroup, String codeName, int page, int size) {
        int offset = (page - 1) * size;
        int totalElements = codeGroupMapper.countTotal();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        CodeGroupSearchCondition condition = CodeGroupSearchCondition.builder()
                .groupCode(codeGroup)
                .groupName(codeName)
                .build();

        List<CodeGroupDTO> codeGroupDTOS = codeGroupMapper.seletByCondition(condition, offset, size);

        return PageDTO.<CodeGroupDTO>builder()
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .content(codeGroupDTOS)
                .build();
    }

    @Override
    public CodeGroupDTO getCodeGroup(String groupCode) {

        return codeGroupMapper.selectByGroupCode(groupCode).orElseThrow(() -> new BusinessException(ErrorCode.NO_DATA_FOUND));
    }
}
