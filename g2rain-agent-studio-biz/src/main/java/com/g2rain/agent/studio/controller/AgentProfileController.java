package com.g2rain.agent.studio.controller;

import com.g2rain.agent.studio.api.AgentProfileApi;
import com.g2rain.agent.studio.dto.AgentProfileDto;
import com.g2rain.agent.studio.dto.AgentProfileSelectDto;
import com.g2rain.agent.studio.service.AgentProfileService;
import com.g2rain.agent.studio.vo.AgentBundleVo;
import com.g2rain.agent.studio.vo.AgentCatalogItemVo;
import com.g2rain.agent.studio.vo.AgentProfileVo;
import com.g2rain.agent.studio.vo.SkillDigestVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.web.interceptors.annotations.IdentityInject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数字员工画像表控制器
 * 表名: agent_profile
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/agent_profile")
public class AgentProfileController implements AgentProfileApi {

    @Resource(name = "agentProfileServiceImpl")
    private AgentProfileService agentProfileService;

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<List<AgentProfileVo>> selectList(AgentProfileSelectDto selectDto) {
        return Result.success(agentProfileService.selectList(selectDto));
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<List<AgentCatalogItemVo>> selectActiveCatalog() {
        return Result.success(agentProfileService.selectActiveCatalog());
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<AgentCatalogItemVo> resolveProfile(Long agentId) {
        return Result.success(agentProfileService.resolveProfile(agentId));
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<List<SkillDigestVo>> selectSkillDigest(Long agentId) {
        return Result.success(agentProfileService.selectSkillDigest(agentId));
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<AgentBundleVo> selectCapabilityBundle(Long agentId) {
        return Result.success(agentProfileService.selectCapabilityBundle(agentId));
    }

    @Override
    @IdentityInject(organIdPropertyName = "organId")
    public Result<PageData<AgentProfileVo>> selectPage(PageSelectListDto<AgentProfileSelectDto> selectDto) {
        return Result.successPage(agentProfileService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数字员工画像表信息", description = "新增或更新数字员工画像表基础信息")
    public Result<Long> save(@RequestBody AgentProfileDto dto) {
        return Result.success(agentProfileService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数字员工画像表记录", description = "根据主键删除数字员工画像表记录")
    public Result<Integer> delete(@Parameter(description = "数字员工画像表标识") @PathVariable Long id) {
        return Result.success(agentProfileService.delete(id));
    }
}
