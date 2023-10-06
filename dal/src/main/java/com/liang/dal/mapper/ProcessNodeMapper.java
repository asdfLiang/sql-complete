package com.liang.dal.mapper;

import com.liang.dal.entity.ProcessNodeDO;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @since 2023/9/9 15:46
 * @author by liangzj
 */
@Mapper
public interface ProcessNodeMapper {

    @Insert({
        "<script>",
        " INSERT INTO process_node(node_id, node_type, parent_id, process_id, create_time, modify_time) ",
        " VALUES (#{nodeId}, #{nodeType}, #{parentId}, #{processId}, datetime(), datetime())",
        "</script>"
    })
    Integer insert(ProcessNodeDO processNodeDO);

    @Delete({"DELETE FROM process_node WHERE node_id = #{nodeId}"})
    Integer delete(@Param("nodeId") String nodeId);

    @Select({
        "<script>",
        " SELECT * FROM process_node ",
        " WHERE ",
        "   process_id IN <foreach collection='processIds' item='processId' open='(' separator=',' close=')'> #{processId} </foreach>",
        "   AND node_type = 'ROOT'",
        "</script>"
    })
    List<ProcessNodeDO> selectRoot(@Param("processIds") List<String> processIds);

    @Select({"SELECT * FROM process_node  WHERE process_id = #{processId}"})
    List<ProcessNodeDO> select(@Param("processId") String processId);
}
