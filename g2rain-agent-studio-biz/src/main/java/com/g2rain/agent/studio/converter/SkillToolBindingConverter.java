package com.g2rain.agent.studio.converter;

import com.g2rain.agent.studio.dao.po.SkillToolBindingPo;
import com.g2rain.agent.studio.dto.SkillToolBindingDto;
import com.g2rain.agent.studio.vo.SkillToolBindingVo;
import com.g2rain.common.converter.CommonConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

/**
 * Skill Tool 绑定表转换器
 * 用于Po、Vo、Dto之间的相互转换
 * 表名: skill_tool_binding
 *
 * @author G2rain Generator
 */
@Mapper(uses = CommonConverter.class)
public interface SkillToolBindingConverter {

    /**
     * 单例实例，通过 {@link Mappers#getMapper(Class)} 获取 MapStruct 自动生成的实现。
     */
    SkillToolBindingConverter INSTANCE = Mappers.getMapper(SkillToolBindingConverter.class);

    /**
     * Po -> Vo
     * 自动将 createTime 和 updateTime 从 {@link LocalDateTime} 转换为 {@link String}
     */
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "localDateTimeToString")
    SkillToolBindingVo po2vo(SkillToolBindingPo po);

    /**
     * Dto -> Po
     * 自动将 createTime 和 updateTime 从 {@link String} 转换为 {@link LocalDateTime}
     * 忽略 version 字段
     * 忽略 deleteFlag 字段
     */
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleteFlag", ignore = true)
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "stringToLocalDateTime")
    SkillToolBindingPo dto2po(SkillToolBindingDto dto);
}
