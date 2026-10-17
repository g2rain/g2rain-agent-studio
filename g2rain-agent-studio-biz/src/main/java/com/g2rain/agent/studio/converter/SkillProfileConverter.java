package com.g2rain.agent.studio.converter;

import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dto.SkillProfileDto;
import com.g2rain.agent.studio.vo.SkillProfileVo;
import com.g2rain.common.converter.CommonConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

/**
 * Skill 定义表转换器
 * 用于Po、Vo、Dto之间的相互转换
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@Mapper(uses = CommonConverter.class)
public interface SkillProfileConverter {

    /**
     * 单例实例，通过 {@link Mappers#getMapper(Class)} 获取 MapStruct 自动生成的实现。
     */
    SkillProfileConverter INSTANCE = Mappers.getMapper(SkillProfileConverter.class);

    /**
     * Po -> Vo
     * 自动将 createTime 和 updateTime 从 {@link LocalDateTime} 转换为 {@link String}
     */
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "localDateTimeToString")
    SkillProfileVo po2vo(SkillProfilePo po);

    /**
     * Dto -> Po
     * 自动将 createTime 和 updateTime 从 {@link String} 转换为 {@link LocalDateTime}
     * 忽略 version 字段
     * 忽略 deleteFlag 字段
     */
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleteFlag", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "stringToLocalDateTime")
    SkillProfilePo dto2po(SkillProfileDto dto);
}
