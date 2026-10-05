package com.g2rain.agent.studio.converter;

import com.g2rain.agent.studio.dao.po.AgentProfilePo;
import com.g2rain.agent.studio.dto.AgentProfileDto;
import com.g2rain.agent.studio.vo.AgentCatalogItemVo;
import com.g2rain.agent.studio.vo.AgentProfileVo;
import com.g2rain.common.converter.CommonConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

/**
 * 数字员工画像表转换器
 * 用于Po、Vo、Dto之间的相互转换
 * 表名: agent_profile
 *
 * @author G2rain Generator
 */
@Mapper(uses = CommonConverter.class)
public interface AgentProfileConverter {

    /**
     * 单例实例，通过 {@link Mappers#getMapper(Class)} 获取 MapStruct 自动生成的实现。
     */
    AgentProfileConverter INSTANCE = Mappers.getMapper(AgentProfileConverter.class);

    /**
     * Po -> Vo
     * 自动将 createTime 和 updateTime 从 {@link LocalDateTime} 转换为 {@link String}
     */
    @Mapping(target = "createTime", source = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updateTime", source = "updateTime", qualifiedByName = "localDateTimeToString")
    AgentProfileVo po2vo(AgentProfilePo po);

    /**
     * Po → 编排用画像摘要（id → agentId，含人设与 status）
     */
    @Mapping(target = "agentId", source = "id")
    AgentCatalogItemVo po2catalog(AgentProfilePo po);

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
    AgentProfilePo dto2po(AgentProfileDto dto);
}
