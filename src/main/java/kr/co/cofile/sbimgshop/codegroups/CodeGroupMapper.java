package kr.co.cofile.sbimgshop.codegroups;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CodeGroupMapper {

    // pk가 group_code 임에 주의
    @Insert("INSERT INTO code_group (group_code, group_name) VALUES (#{groupCode}, #{groupName})")
    int insert(CreateCodeGroupRequest createCodeGroupRequest);

    @Select("SELECT group_code, group_name, use_yn, is_deleted, created_at, updated_at " +
            "FROM  code_group")
    List<CodeGroupDTO> selectAll();

//    @Select("SELECT group_code, group_name, use_yn, is_deleted, created_at, updated_at " +
//            "FROM  code_group ORDER BY group_code DESC LIMIT #{size} OFFSET #{offset}")
    List<CodeGroupDTO> selectByCondition(@Param("condition") CodeGroupSearchCondition condition, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM code_group WHERE group_code = #{groupCode}")
    Optional<CodeGroupDTO> selectByGroupCode(@Param("groupCode") String groupCode);

    @Select("SELECT EXISTS(SELECT 1 FROM code_group WHERE group_code = #{groupCode})")
    boolean exists(@Param("groupCode") String groupCode);

    @Delete("DELETE FROM code_group WHERE group_code = #{groupCode}")
    int delete(@Param("groupCode") String groupCode);

    @Select("SELECT count(*) FROM code_group")
    int countTotal();

    @Update("UPDATE code_group SET group_name = #{updateCodeGroupRequest.groupName}, use_yn = #{updateCodeGroupRequest.useYn} " +
            "WHERE group_code = #{groupCode}")
    int updateCodeGroup(@Param("groupCode") String groupCode, @Param("updateCodeGroupRequest") UpdateCodeGroupRequest updateCodeGroupRequest);

    int partialUpdateCodeGroup(@Param("groupCode") String groupCode, @Param("updates") Map<String, Object> updateCodeGroupRequest);
}