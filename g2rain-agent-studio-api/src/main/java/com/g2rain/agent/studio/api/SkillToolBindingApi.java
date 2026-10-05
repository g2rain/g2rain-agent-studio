package com.g2rain.agent.studio.api;

import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.vo.SkillToolBindingVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * Skill Tool 绑定表API接口
 * 表名: skill_tool_binding
 *
 * @author G2rain Generator
 */
@Tag(name = "Skill Tool 绑定表", description = "Skill Tool 绑定表相关接口")
public interface SkillToolBindingApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询Skill Tool 绑定表列表", description = "根据查询条件返回Skill Tool 绑定表列表")
    Result<List<SkillToolBindingVo>> selectList(SkillToolBindingSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询Skill Tool 绑定表列表", description = "分页查询Skill Tool 绑定表列表")
    Result<PageData<SkillToolBindingVo>> selectPage(PageSelectListDto<SkillToolBindingSelectDto> selectDto);
}
