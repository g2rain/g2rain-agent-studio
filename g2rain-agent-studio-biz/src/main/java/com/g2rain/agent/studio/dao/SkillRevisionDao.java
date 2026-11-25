package com.g2rain.agent.studio.dao;

import com.g2rain.agent.studio.dao.po.SkillRevisionPo;
import com.g2rain.agent.studio.dto.SkillRevisionSelectDto;
import com.g2rain.data.isolation.annotations.DataIsolation;
import com.g2rain.data.isolation.annotations.IgnoreIsolation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Skill 修订表数据访问接口
 * 表名: skill_revision
 *
 * @author G2rain Generator
 */
@Mapper
@DataIsolation(organIdPropertyName = "organId", organIdColumnName = "organ_id")
public interface SkillRevisionDao {

    /**
     * 插入单条记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int insert(SkillRevisionPo entity);

    /**
     * 批量插入记录
     *
     * @param list 实体对象列表
     * @return 影响行数
     */
    int insertMultiple(List<SkillRevisionPo> list);

    /**
     * 根据 ID 更新记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int update(SkillRevisionPo entity);

    /**
     * 根据 ID 删除记录
     *
     * @param id 主键 ID
     * @return 影响行数
     */
    int delete(Long id);

    /**
     * 根据ID和Version更新记录（乐观锁更新）
     *
     * @param entity 实体对象（必须包含version字段）
     * @return 影响行数
     */
    int updateByVersion(SkillRevisionPo entity);

    /**
     * 根据 ID 查询记录
     *
     * @param id 主键 ID
     * @return 实体对象
     */
    SkillRevisionPo selectById(Long id);

    /**
     * 根据查询入参 DTO 筛选列表
     *
     * @param selectDto 查询条件 DTO
     * @return 实体对象列表
     */
    List<SkillRevisionPo> selectList(SkillRevisionSelectDto selectDto);

    /**
     * 插入单条记录（忽略数据隔离）
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    @IgnoreIsolation
    int insertWithoutIsolation(SkillRevisionPo entity);

    /**
     * 根据ID更新记录（忽略数据隔离）
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    @IgnoreIsolation
    int updateWithoutIsolation(SkillRevisionPo entity);

    /**
     * 根据ID查询记录（忽略数据隔离）
     *
     * @param id 主键 ID
     * @return 实体对象
     */
    @IgnoreIsolation
    SkillRevisionPo selectByIdWithoutIsolation(Long id);

    /**
     * 根据查询入参DTO筛选列表（忽略数据隔离）
     *
     * @param selectDto 查询条件 DTO
     * @return 实体对象列表
     */
    @IgnoreIsolation
    List<SkillRevisionPo> selectListWithoutIsolation(SkillRevisionSelectDto selectDto);

    /**
     * 技能下未删除修订的最大修订号；无则为 0。
     *
     * @param skillId 技能主键
     * @return 最大修订号
     */
    int selectMaxRevisionNo(@Param("skillId") Long skillId);

    /**
     * 技能当前发布修订。
     *
     * @param skillId 技能主键
     * @return 发布行；没有时为空
     */
    @Nullable
    SkillRevisionPo selectPublished(@Param("skillId") Long skillId);

    /**
     * 条件改状态：仅当当前状态等于期望值时更新。
     *
     * @param id             修订主键
     * @param expectedStatus 期望的当前状态
     * @param newStatus      新状态
     * @param revisionNo     新修订号；可空表示不改
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id,
                     @Param("expectedStatus") String expectedStatus,
                     @Param("newStatus") String newStatus,
                     @Param("revisionNo") @Nullable Integer revisionNo);

    /**
     * 把技能当前发布修订改为已被替代。
     *
     * @param skillId 技能主键
     * @return 影响行数
     */
    int supersedePublished(@Param("skillId") Long skillId);

    /**
     * 按技能逻辑删除全部修订。
     *
     * @param skillId 技能主键
     * @return 影响行数
     */
    int deleteBySkillId(@Param("skillId") Long skillId);
}
