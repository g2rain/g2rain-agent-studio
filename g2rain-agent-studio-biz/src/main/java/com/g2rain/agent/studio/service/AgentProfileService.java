package com.g2rain.agent.studio.service;

import com.g2rain.agent.studio.dto.AgentProfileDto;
import com.g2rain.agent.studio.dto.AgentProfileSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.vo.AgentBundleVo;
import com.g2rain.agent.studio.vo.AgentCatalogItemVo;
import com.g2rain.agent.studio.vo.AgentOptionVo;
import com.g2rain.agent.studio.vo.AgentProfileVo;
import com.g2rain.agent.studio.vo.SkillDigestVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;

import java.util.List;

/**
 * 数字员工画像表服务接口
 * 表名: agent_profile
 *
 * @author G2rain Generator
 */
public interface AgentProfileService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<AgentProfileVo> selectList(AgentProfileSelectDto selectDto);

    /**
     * 平台智能体列表：固定 PLATFORM，不按机构隔离。
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<AgentProfileVo> selectPlatformList(AgentProfileSelectDto selectDto);

    /**
     * 查询 ACTIVE 数字员工目录（PLATFORM 不隔离 + CUSTOM 本租户）。
     *
     * @return ACTIVE Agent 摘要列表
     */
    List<AgentCatalogItemVo> selectActiveCatalog();

    /**
     * 查询 ACTIVE 数字员工下拉选项（PLATFORM 不隔离 + CUSTOM 本租户；仅 id + 名称）。
     *
     * @return ACTIVE Agent 选项列表
     */
    List<AgentOptionVo> selectActiveOptions();

    /**
     * 按 agentId 解析画像（含 status，不筛 ACTIVE）。
     *
     * @param agentId 数字员工标识
     * @return 画像摘要；不存在为 null
     */
    AgentCatalogItemVo resolveProfile(Long agentId);

    /**
     * 按 agentId 查询 ACTIVE Skill 短目录（不含手册 / 契约 / tools）。
     *
     * @param agentId 数字员工标识
     * @return Skill 短目录；无技能为空列表
     */
    List<SkillDigestVo> selectSkillDigest(Long agentId);

    /**
     * 按 agentId 查询能力包：Skill（手册/契约）+ 其下 Tool 列表。
     *
     * @param agentId 数字员工标识
     * @return 能力包；无技能时 skills 为空列表
     */
    AgentBundleVo selectCapabilityBundle(Long agentId);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<AgentProfileVo> selectPage(PageSelectListDto<AgentProfileSelectDto> selectDto);

    /**
     * 平台智能体分页：固定 PLATFORM，不按机构隔离。
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<AgentProfileVo> selectPlatformPage(PageSelectListDto<AgentProfileSelectDto> selectDto);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(AgentProfileDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);

    /**
     * 仅修改状态。
     *
     * @param id  主键
     * @param dto 状态
     * @return 影响行数
     */
    int updateStatus(Long id, UpdateStatusDto dto);
}
