package com.liang.dal.mapper;

import com.liang.dal.entity.ProcessDO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @since 2023/9/9 15:46
 * @author by liangzj
 */
@Mapper
public interface ProcessMapper {

    @Insert({
        "<script>",
        "INSERT INTO process(process_id, process_name) VALUES (#{processId}, #{processName})",
        "</script>"
    })
    Integer insert(ProcessDO processDO);

    @Select({"<script>", "SELECT * FROM process ORDER BY id DESC", "</script>"})
    List<ProcessDO> selectAll();

    @Select({"SELECT * FROM process WHERE process_id = #{processId}"})
    ProcessDO selectOne(@Param("processId") String processId);
}
