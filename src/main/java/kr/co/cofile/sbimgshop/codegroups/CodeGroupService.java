package kr.co.cofile.sbimgshop.codegroups;

import kr.co.cofile.sbimgshop.common.dto.PageDTO;

import java.util.Map;

public interface CodeGroupService {

    void createCodeGroup(CreateCodeGroupRequest createCodeGroupRequest);
//    List<CodeGroupDTO> findBy
    PageDTO<CodeGroupResponse> getCodeGroups(int page, int size);

    PageDTO<CodeGroupResponse> getCodeGroups(String codeGroup, String codeName, int page, int size);

    CodeGroupDTO getCodeGroup(String groupCode);

    CodeGroupDTO updateCodeGroup(String codeGroup, UpdateCodeGroupRequest updateCodeGroupRequest);

    CodeGroupDTO partialUpdateCodeGroup(String codeGroup, Map<String, Object> updates);

    void deleteCodeGroup(CodeGroupDTO codeGroupDTO);
}
