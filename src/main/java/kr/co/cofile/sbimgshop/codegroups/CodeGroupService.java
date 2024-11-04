package kr.co.cofile.sbimgshop.codegroups;

import kr.co.cofile.sbimgshop.common.dto.PageDTO;

public interface CodeGroupService {

    void register(CodeGroupDTO codeGroupDTO);
//    List<CodeGroupDTO> findBy
    PageDTO getCodeGroups(int page, int size);
}
