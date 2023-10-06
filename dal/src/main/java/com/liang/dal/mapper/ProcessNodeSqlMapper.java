package com.liang.dal.mapper;

import com.liang.dal.entity.ProcessNodeSqlDO;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @since 2023/9/9 15:46
 * @author by liangzj
 */
@Mapper
public interface ProcessNodeSqlMapper {

    @Insert({
        "<script>",
        " INSERT OR REPLACE INTO process_node_sql(process_id, node_id, connection_id, sql_text, create_time, modify_time) ",
        " VALUES (#{processId}, #{nodeId}, #{connectionId}, #{sqlText}, datetime(), datetime())",
        "</script>"
    })
    Integer insertOrUpdate(ProcessNodeSqlDO processNodeSqlDO);

    @Delete({"DELETE FROM process_node_sql WHERE node_id = #{nodeId}"})
    Integer delete(@Param("nodeId") String nodeId);

    @Select({"SELECT * FROM process_node_sql WHERE process_id = #{processId}"})
    List<ProcessNodeSqlDO> select(@Param("processId") String processId);

    @Select({
        "<script>",
        " SELECT * FROM process_node_sql ",
        " WHERE ",
        "   node_id IN <foreach collection='nodeIds' item='nodeId' open='(' separator=',' close=')'> #{nodeId} </foreach>",
        "</script>"
    })
    List<ProcessNodeSqlDO> selectBatch(@Param("nodeIds") List<String> nodeIds);
}
