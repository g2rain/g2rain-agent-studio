package com.g2rain.agent.studio.service;

import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.agent.studio.dto.CreateSkillRevisionDraftDto;
import com.g2rain.agent.studio.dto.PublishSkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillRevisionSelectDto;
import com.g2rain.agent.studio.vo.SkillRevisionVo;

import java.util.List;

/**
 * Skill 修订表服务接口
 * 表名: skill_revision
 *
 * @author G2rain Generator
 */
public interface SkillRevisionService {

    /**
     * 根据条件查询列表
     *
     * @param selectDto 查询条件 DTO
     * @return VO 对象列表
     */
    List<SkillRevisionVo> selectList(SkillRevisionSelectDto selectDto);

    /**
     * 根据条件分页查询
     *
     * @param selectDto 查询条件DTO（包含分页参数）
     * @return 分页 VO 数据
     */
    PageData<SkillRevisionVo> selectPage(PageSelectListDto<SkillRevisionSelectDto> selectDto);

    /**
     * 按主键查询修订。
     *
     * @param id 修订主键
     * @return 修订详情
     */
    SkillRevisionVo selectById(Long id);

    /**
     * 新增或更新数据
     *
     * @param dto 数据传输对象
     * @return 操作结果（影响行数）
     */
    Long save(SkillRevisionDto dto);

    /**
     * 根据 ID 删除数据
     *
     * @param id 主键 ID
     * @return 操作结果（影响行数）
     */
    int delete(Long id);

    /**
     * 保存技能或工具绑定后留痕：上一条发布改为已被替代，新增一条发布修订。
     *
     * @param skillId 技能主键
     */
    void recordPublishedSnapshot(Long skillId);

    /**
     * 删除技能时逻辑删除其全部修订。
     *
     * @param skillId 技能主键
     */
    void deleteBySkillId(Long skillId);

    /**
     * 以当前发布修订为底创建候选草稿。
     *
     * @param dto 技能与可选覆盖字段
     * @return 新草稿主键
     */
    Long createDraft(CreateSkillRevisionDraftDto dto);

    /**
     * 发布草稿：快照写回技能与绑定，本条升为发布。
     *
     * @param id  草稿主键
     * @param dto 期望的当前发布修订号
     */
    void publish(Long id, PublishSkillRevisionDto dto);

    /**
     * 丢弃草稿。
     *
     * @param id 草稿主键
     */
    void discard(Long id);

    /**
     * 回滚到指定非草稿修订并生效。
     *
     * @param id 历史修订主键
     */
    void rollback(Long id);
}