package com.g2rain.agent.studio.dao;

import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.data.isolation.annotations.DataIsolation;
import com.g2rain.data.isolation.annotations.IgnoreIsolation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Skill 定义表数据访问接口
 * 表名: skill_profile
 *
 * @author G2rain Generator
 */
@Mapper
@DataIsolation(organIdPropertyName = "organId", organIdColumnName = "organ_id")
public interface SkillProfileDao {

    /**
     * 插入单条记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int insert(SkillProfilePo entity);

    /**
     * 批量插入记录
     *
     * @param list 实体对象列表
     * @return 影响行数
     */
    int insertMultiple(List<SkillProfilePo> list);

    /**
     * 根据 ID 更新记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int update(SkillProfilePo entity);

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
    int updateByVersion(SkillProfilePo entity);

    /**
     * 根据 ID 查询记录
     *
     * @param id 主键 ID
     * @return 实体对象
     */
    SkillProfilePo selectById(Long id);

    /**
     * 根据查询入参 DTO 筛选列表
     *
     * @param selectDto 查询条件 DTO
     * @return 实体对象列表
     */
    List<SkillProfilePo> selectList(SkillProfileSelectDto selectDto);

    /**
     * 插入单条记录（忽略数据隔离）
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    @IgnoreIsolation
    int insertWithoutIsolation(SkillProfilePo entity);

    /**
     * 根据ID更新记录（忽略数据隔离）
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    @IgnoreIsolation
    int updateWithoutIsolation(SkillProfilePo entity);

    /**
     * 根据ID查询记录（忽略数据隔离）
     *
     * @param id 主键 ID
     * @return 实体对象
     */
    @IgnoreIsolation
    SkillProfilePo selectByIdWithoutIsolation(Long id);

    /**
     * 根据查询入参DTO筛选列表（忽略数据隔离）
     *
     * @param selectDto 查询条件 DTO
     * @return 实体对象列表
     */
    @IgnoreIsolation
    List<SkillProfilePo> selectListWithoutIsolation(SkillProfileSelectDto selectDto);

    /**
     * 按条件查询 Skill 短目录字段（不含手册 / 契约），本租户隔离。
     *
     * @param selectDto 查询条件 DTO（常用 ids + status + resourceType）
     * @return 短字段 PO 列表；manual/contract 为空
     */
    List<SkillProfilePo> selectDigestList(SkillProfileSelectDto selectDto);

    /**
     * 按条件查询 Skill 短目录字段（不含手册 / 契约），忽略数据隔离。
     *
     * @param selectDto 查询条件 DTO（常用 ids + status + resourceType=PLATFORM）
     * @return 短字段 PO 列表；manual/contract 为空
     */
    @IgnoreIsolation
    List<SkillProfilePo> selectDigestListWithoutIsolation(SkillProfileSelectDto selectDto);
}
