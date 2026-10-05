package com.g2rain.agent.studio.controller;

import com.g2rain.agent.studio.api.AgentSkillBindingApi;
import com.g2rain.agent.studio.dto.AgentSkillBindingDto;
import com.g2rain.agent.studio.dto.AgentSkillBindingSelectDto;
import com.g2rain.agent.studio.service.AgentSkillBindingService;
import com.g2rain.agent.studio.vo.AgentSkillBindingVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
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
 * 数字员工 Skill 绑定表控制器
 * 表名: agent_skill_binding
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/agent_skill_binding")
public class AgentSkillBindingController implements AgentSkillBindingApi {

    @Resource(name = "agentSkillBindingServiceImpl")
    private AgentSkillBindingService agentSkillBindingService;

    @Override
    public Result<List<AgentSkillBindingVo>> selectList(AgentSkillBindingSelectDto selectDto) {
        return Result.success(agentSkillBindingService.selectList(selectDto));
    }

    @Override
    public Result<PageData<AgentSkillBindingVo>> selectPage(PageSelectListDto<AgentSkillBindingSelectDto> selectDto) {
        return Result.successPage(agentSkillBindingService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新数字员工 Skill 绑定表信息", description = "新增或更新数字员工 Skill 绑定表基础信息")
    public Result<Long> save(@RequestBody AgentSkillBindingDto dto) {
        return Result.success(agentSkillBindingService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数字员工 Skill 绑定表记录", description = "根据主键删除数字员工 Skill 绑定表记录")
    public Result<Integer> delete(@Parameter(description = "数字员工 Skill 绑定表标识") @PathVariable Long id) {
        return Result.success(agentSkillBindingService.delete(id));
    }
}
