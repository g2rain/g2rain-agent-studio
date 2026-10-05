package com.g2rain.agent.studio.api;

import com.g2rain.agent.studio.dto.AgentSkillBindingSelectDto;
import com.g2rain.agent.studio.vo.AgentSkillBindingVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 数字员工 Skill 绑定表API接口
 * 表名: agent_skill_binding
 *
 * @author G2rain Generator
 */
@Tag(name = "数字员工 Skill 绑定表", description = "数字员工 Skill 绑定表相关接口")
public interface AgentSkillBindingApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询数字员工 Skill 绑定表列表", description = "根据查询条件返回数字员工 Skill 绑定表列表")
    Result<List<AgentSkillBindingVo>> selectList(AgentSkillBindingSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询数字员工 Skill 绑定表列表", description = "分页查询数字员工 Skill 绑定表列表")
    Result<PageData<AgentSkillBindingVo>> selectPage(PageSelectListDto<AgentSkillBindingSelectDto> selectDto);
}
