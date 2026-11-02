package com.g2rain.agent.studio.converter;

import com.g2rain.common.converter.CommonConverter;
import com.g2rain.agent.studio.dao.po.ChatPromptTemplatePo;
import com.g2rain.agent.studio.dto.ChatPromptTemplateDto;
import com.g2rain.agent.studio.vo.ChatPromptTemplateVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

/**
 * Chat 问题模板转换器
 * 用于Po、Vo、Dto之间的相互转换
 * 表名: chat_prompt_template
 *
 * @author G2rain Generator
 */
@Mapper(uses = CommonConverter.class)
public interface ChatPromptTemplateConverter {

    /**
     * 单例实例，通过 {@link Mappers#getMapper(Class)} 获取 MapStruct 自动生成的实现。
     */
    ChatPromptTemplateConverter INSTANCE = Mappers.getMapper(ChatPromptTemplateConverter.class);

    /**
     * Po -> Vo
     * 自动将 createTime 和 updateTime 从 {@link LocalDateTime} 转换为 {@link String}
     */
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "localDateTimeToString")
    ChatPromptTemplateVo po2vo(ChatPromptTemplatePo po);

    /**
     * Dto -> Po
     * 自动将 createTime 和 updateTime 从 {@link String} 转换为 {@link LocalDateTime}
     * 忽略 version 字段
     * 忽略 deleteFlag 字段
     * 忽略 status 字段，状态仅通过状态切换接口修改
     */
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleteFlag", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "stringToLocalDateTime")
    ChatPromptTemplatePo dto2po(ChatPromptTemplateDto dto);
}
