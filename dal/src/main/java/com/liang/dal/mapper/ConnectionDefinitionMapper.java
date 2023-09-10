package com.liang.dal.mapper;

import com.liang.dal.entity.ConnectionDefinitionDO;

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
public interface ConnectionDefinitionMapper {

    @Insert({
        "<script>",
        "INSERT INTO connection_definition(connection_id, connection_name, url, database_name, username, password) ",
        "VALUES (#{connectionId}, #{connectionName}, #{url}, #{databaseName}, #{username}, #{password})",
        "</script>"
    })
    Integer insert(ConnectionDefinitionDO connectionDO);

    @Select({"SELECT * FROM connection_definition"})
    List<ConnectionDefinitionDO> selectAll();

    @Select({"SELECT * FROM connection_definition WHERE connection_id = #{connectionId}"})
    ConnectionDefinitionDO selectOne(@Param("connectionId") String connectionId);
}
