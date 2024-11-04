package kr.co.cofile.sbimgshop.codegroups;

import kr.co.cofile.sbimgshop.common.dto.PageDTO;

public interface CodeGroupService {

    void createCodeGroup(CodeGroupDTO codeGroupDTO);
//    List<CodeGroupDTO> findBy
    PageDTO getCodeGroups(int page, int size);

    PageDTO getCodeGroups(String codeGroup, String codeName, int page, int size);

    CodeGroupDTO getCodeGroup(String groupCode);
}
