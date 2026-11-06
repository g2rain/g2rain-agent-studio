package com.g2rain.agent.studio.api;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import com.g2rain.agent.studio.dto.AgentSessionBindingSelectDto;
import com.g2rain.agent.studio.vo.AgentSessionBindingVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 智能体会话绑定表API接口
 * 表名: agent_session_binding
 *
 * @author G2rain Generator
 */
@Tag(name = "智能体会话绑定表", description = "智能体会话绑定表相关接口")
public interface AgentSessionBindingApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询智能体会话绑定表列表", description = "根据查询条件返回智能体会话绑定表列表")
    Result<List<AgentSessionBindingVo>> selectList(AgentSessionBindingSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询智能体会话绑定表列表", description = "分页查询智能体会话绑定表列表")
    Result<PageData<AgentSessionBindingVo>> selectPage(PageSelectListDto<AgentSessionBindingSelectDto> selectDto);
}