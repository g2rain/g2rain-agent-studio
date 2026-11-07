package com.g2rain.agent.studio.controller;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.agent.studio.api.AgentSessionBindingApi;
import com.g2rain.agent.studio.dto.AgentSessionAssignDto;
import com.g2rain.agent.studio.dto.AgentSessionBindingDto;
import com.g2rain.agent.studio.dto.AgentSessionBindingSelectDto;
import com.g2rain.agent.studio.service.AgentSessionBindingService;
import com.g2rain.agent.studio.vo.AgentSessionBindingVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 智能体会话绑定表控制器
 * 表名: agent_session_binding
 *
 * @author G2rain Generator
 */
@RestController
@RequestMapping("/agent_session_binding")
public class AgentSessionBindingController implements AgentSessionBindingApi {

    @Resource(name = "agentSessionBindingServiceImpl")
    private AgentSessionBindingService agentSessionBindingService;

    @Override
    public Result<List<AgentSessionBindingVo>> selectList(AgentSessionBindingSelectDto selectDto) {
        return Result.success(agentSessionBindingService.selectList(selectDto));
    }

    @Override
    public Result<PageData<AgentSessionBindingVo>> selectPage(PageSelectListDto<AgentSessionBindingSelectDto> selectDto) {
        return Result.successPage(agentSessionBindingService.selectPage(selectDto));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或更新智能体会话绑定表信息", description = "新增或更新智能体会话绑定表基础信息")
    public Result<Long> save(@RequestBody AgentSessionBindingDto dto) {
        return Result.success(agentSessionBindingService.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除智能体会话绑定表记录", description = "根据主键删除智能体会话绑定表记录")
    public Result<Integer> delete(@Parameter(description = "智能体会话绑定表标识") @PathVariable Long id) {
        return Result.success(agentSessionBindingService.delete(id));
    }

    @PostMapping("/assign")
    @Operation(summary = "智能体访问授权", description = "按提交的会话类型全量同步智能体可访问的会话主体")
    public Result<Integer> assign(@RequestBody @Validated AgentSessionAssignDto dto) {
        return Result.success(agentSessionBindingService.assign(dto));
    }
}
