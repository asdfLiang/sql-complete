package com.liang.dal.mapper;

import com.liang.dal.entity.ProcessDO;
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
        "INSERT INTO process_node(node_id, node_type, parent_id, process_id) ",
        "VALUES (#{nodeId}, #{nodeType}, #{parentId}, #{processId})",
        "</script>"
    })
    Integer insert(ProcessNodeDO processNodeDO);

    @Select({"SELECT * FROM process_node WHERE process_id = #{processId}"})
    List<ProcessDO> select(@Param("processId") String processId);

    @Delete({"DELETE FROM process_node WHERE node_id = #{nodeId}"})
    Integer delete(@Param("nodeId") String nodeId);
}
