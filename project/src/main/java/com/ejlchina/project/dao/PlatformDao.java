package com.ejlchina.project.dao;

import com.ejlchina.project.entity.Platform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlatformDao {

    @Select("select * from platform")
    List<Platform> getPlatforms();

    @Select("select * from platform p where id = #{id}")
    Platform getPlatform(Long id);

}
