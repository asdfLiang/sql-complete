package com.liang.dal.mapper;

import com.liang.dal.entity.ProcessSessionDO;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @since 2023/9/9 15:46
 * @author by liangzj
 */
@Mapper
public interface ProcessSessionMapper {

    @Insert({
        "<script>",
        "INSERT INTO process_session(session_id, process_id, create_time, modify_time) VALUES (#{sessionId}, #{processId}, datetime(), datetime())",
        "</script>"
    })
    Integer insert(ProcessSessionDO sessionDO);

    @Select({"SELECT * FROM process_session"})
    List<ProcessSessionDO> selectAll();

    @Select({"SELECT * FROM process_session WHERE session_id = #{sessionId}"})
    ProcessSessionDO selectOne(@Param("sessionId") String sessionId);

    @Select({"SELECT * FROM process_session WHERE process_id = #{processId}"})
    ProcessSessionDO selectByProcessId(@Param("processId") String processId);

    @Delete({"DELETE FROM process_session WHERE session_id = #{sessionId}"})
    Integer delete(String sessionId);
}
