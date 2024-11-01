package kr.co.cofile.sbimgshop.codegroups;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeGroupMapper {

    // pk가 group_code 임에 주의
    @Insert("INSERT INTO code_group (group_code, group_name) VALUES (#{groupCode}, #{groupName})")
    void create(CodeGroupDTO codeGroupDTO);

}