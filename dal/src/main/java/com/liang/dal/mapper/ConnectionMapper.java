package com.liang.dal.mapper;

import com.liang.dal.entity.ConnectionDO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @since 2023/9/9 15:46
 * @author by liangzj
 */
@Mapper
public interface ConnectionMapper {

    @Insert({
        "<script>",
        "INSERT INTO connection(connection_id, connection_name, url, database_name, username, password) ",
        "VALUES (#{connectionId}, #{connectionName}, #{url}, #{databaseName}, #{username}, #{password})",
        "</script>"
    })
    Integer insert(ConnectionDO connectionDO);

    @Select({"SELECT * FROM connection"})
    List<ConnectionDO> selectAll();
}
