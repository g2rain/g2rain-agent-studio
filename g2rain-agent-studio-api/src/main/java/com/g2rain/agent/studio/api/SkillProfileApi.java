package com.g2rain.agent.studio.api;

import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.agent.studio.vo.SkillProfileVo;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * Skill 定义表API接口
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@Tag(name = "Skill 定义表", description = "Skill 定义表相关接口")
public interface SkillProfileApi {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return 数据列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询Skill 定义表列表", description = "根据查询条件返回Skill 定义表列表")
    Result<List<SkillProfileVo>> selectList(SkillProfileSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询Skill 定义表列表", description = "分页查询Skill 定义表列表")
    Result<PageData<SkillProfileVo>> selectPage(PageSelectListDto<SkillProfileSelectDto> selectDto);
}
