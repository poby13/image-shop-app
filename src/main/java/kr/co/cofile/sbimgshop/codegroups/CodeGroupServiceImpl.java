package kr.co.cofile.sbimgshop.codegroups;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeGroupServiceImpl implements CodeGroupService {

    private final CodeGroupMapper codeGroupMapper;


    @Override
    public void register(CodeGroupDTO codeGroupDTO) {
        // TODO DuplicateKeyException
        codeGroupMapper.create(codeGroupDTO);
    }

}
