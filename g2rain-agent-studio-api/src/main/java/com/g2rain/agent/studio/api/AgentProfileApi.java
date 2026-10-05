package com.g2rain.agent.studio.api;

import com.g2rain.agent.studio.dto.AgentProfileSelectDto;
import com.g2rain.agent.studio.vo.AgentBundleVo;
import com.g2rain.agent.studio.vo.AgentCatalogItemVo;
import com.g2rain.agent.studio.vo.AgentProfileVo;
import com.g2rain.agent.studio.vo.SkillDigestVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/**
 * 数字员工画像表API接口
 * 表名: agent_profile
 *
 * @author G2rain Generator
 */
@Tag(name = "数字员工画像表", description = "数字员工画像表相关接口")
public interface AgentProfileApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询数字员工画像表列表", description = "根据查询条件返回数字员工画像表列表")
    Result<List<AgentProfileVo>> selectList(AgentProfileSelectDto selectDto);

    /**
     * 查询 ACTIVE 数字员工目录（PLATFORM 不隔离 + CUSTOM 本租户），供意图选人候选。
     *
     * @return ACTIVE Agent 摘要列表（含人设段落）
     */
    @GetMapping("/active_catalog")
    @Operation(summary = "查询 ACTIVE 数字员工目录", hidden = true,
        description = "并行拉取 PLATFORM（不隔离）与 CUSTOM（租户隔离）的 ACTIVE 画像，按 agentId 排序")
    Result<List<AgentCatalogItemVo>> selectActiveCatalog();

    /**
     * 按 agentId 解析画像（含 status，不筛 ACTIVE），供入口校验不存在 / 未启用。
     *
     * <p>先查本租户 CUSTOM，未命中再查 PLATFORM（不隔离）；无记录返回 null。
     *
     * @param agentId 数字员工标识
     * @return 画像摘要（含 status）；不存在为 null
     */
    @GetMapping("/{agentId}/resolve")
    @Operation(summary = "解析数字员工画像", hidden = true,
        description = "按 id 返回画像含 status；CUSTOM 租户隔离，PLATFORM 不隔离；不存在返回空")
    Result<AgentCatalogItemVo> resolveProfile(@Parameter(description = "数字员工标识") @PathVariable("agentId") Long agentId);

    /**
     * 按 agentId 查询绑定 Skill 短目录（不含手册 / 契约 / tools），供意图 prompt。
     *
     * @param agentId 数字员工标识
     * @return Skill 短目录列表
     */
    @GetMapping("/{agentId}/skill_digest")
    @Operation(summary = "查询 Skill 短目录", hidden = true,
        description = "返回该 Agent 启用绑定的 ACTIVE Skill 短字段（key/name/semanticDesc）")
    Result<List<SkillDigestVo>> selectSkillDigest(@Parameter(description = "数字员工标识") @PathVariable("agentId") Long agentId);

    /**
     * 按 agentId 查询能力包：Skill（含手册/契约）及其绑定 Tool 列表，供 hydrate / 执行。
     *
     * @param agentId 数字员工标识
     * @return 能力包；无绑定时 skills 为空列表
     */
    @GetMapping("/{agentId}/capability_bundle")
    @Operation(summary = "查询数字员工能力包", hidden = true,
        description = "按 agentId 返回启用 Skill 完整字段，tools 挂在各 Skill 下")
    Result<AgentBundleVo> selectCapabilityBundle(@Parameter(description = "数字员工标识") @PathVariable("agentId") Long agentId);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询数字员工画像表列表", description = "分页查询数字员工画像表列表")
    Result<PageData<AgentProfileVo>> selectPage(PageSelectListDto<AgentProfileSelectDto> selectDto);
}
