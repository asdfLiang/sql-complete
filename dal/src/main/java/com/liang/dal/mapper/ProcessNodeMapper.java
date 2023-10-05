package com.liang.dal.mapper;

import com.liang.dal.entity.ProcessNodeDO;

import org.apache.ibatis.annotations.*;

/**
 * @since 2023/9/9 15:46
 * @author by liangzj
 */
@Mapper
public interface ProcessNodeMapper {

    @Insert({
        "<script>",
        "INSERT INTO process_node(node_id, node_type, parent_id, process_id, create_time, modify_time) ",
        "VALUES (#{nodeId}, #{nodeType}, #{parentId}, #{processId}, datetime(), datetime())",
        "</script>"
    })
    Integer insert(ProcessNodeDO processNodeDO);

    @Delete({"DELETE FROM process_node WHERE node_id = #{nodeId}"})
    Integer delete(@Param("nodeId") String nodeId);
}
