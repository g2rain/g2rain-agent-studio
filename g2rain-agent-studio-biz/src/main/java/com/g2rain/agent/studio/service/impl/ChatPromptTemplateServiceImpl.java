package com.g2rain.agent.studio.service.impl;

import com.g2rain.common.exception.SystemErrorCode;
import com.g2rain.common.id.IdGenerator;
import com.g2rain.common.model.PageData;
import com.g2rain.common.model.PageSelectListDto;
import com.g2rain.common.utils.Asserts;
import com.g2rain.common.utils.Moments;
import com.g2rain.common.validation.Validations;
import com.g2rain.agent.studio.converter.ChatPromptTemplateConverter;
import com.g2rain.agent.studio.dao.ChatPromptTemplateDao;
import com.g2rain.agent.studio.dao.po.ChatPromptTemplatePo;
import com.g2rain.agent.studio.dto.ChatPromptTemplateDto;
import com.g2rain.agent.studio.dto.ChatPromptTemplateSelectDto;
import com.g2rain.agent.studio.dto.UpdateStatusDto;
import com.g2rain.agent.studio.enums.ResourceStatus;
import com.g2rain.agent.studio.service.ChatPromptTemplateService;
import com.g2rain.agent.studio.vo.ChatPromptTemplateVo;
import com.g2rain.mybatis.pagination.PageContext;
import com.g2rain.mybatis.pagination.model.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Chat 问题模板服务实现类
 * 表名: chat_prompt_template
 *
 * @author G2rain Generator
 */
@Service(value = "chatPromptTemplateServiceImpl")
public class ChatPromptTemplateServiceImpl implements ChatPromptTemplateService {

    @Resource(name = "chatPromptTemplateDao")
    private ChatPromptTemplateDao chatPromptTemplateDao;

    private IdGenerator idGenerator;

    @Qualifier("idGenerator")
    @Autowired(required = false)
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<ChatPromptTemplateVo> selectList(ChatPromptTemplateSelectDto selectDto) {
        return chatPromptTemplateDao.selectList(selectDto)
            .stream()
            .map(ChatPromptTemplateConverter.INSTANCE::po2vo)
            .toList();
    }

    @Override
    public PageData<ChatPromptTemplateVo> selectPage(PageSelectListDto<ChatPromptTemplateSelectDto> selectDto) {
        Page<ChatPromptTemplatePo> page = PageContext.of(selectDto.getPageNum(), selectDto.getPageSize(), () -> {
            chatPromptTemplateDao.selectList(selectDto.getQuery());
        });
        List<ChatPromptTemplateVo> result = page.getResult()
            .stream()
            .map(ChatPromptTemplateConverter.INSTANCE::po2vo)
            .toList();
        return PageData.of(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    @Override
    public Long save(ChatPromptTemplateDto dto) {
        Validations.validateSave(dto);

        // 转换 DTO 为 PO
        ChatPromptTemplatePo entity = ChatPromptTemplateConverter.INSTANCE.dto2po(dto);

        // 判断是新增还是更新
        Long id = entity.getId();
        if (Objects.isNull(id) || id == 0) {
            // 新增：使用IdGenerator生成主键
            entity.setId(idGenerator.generateId());
            LocalDateTime now = Moments.now();
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            int success = chatPromptTemplateDao.insert(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.CREATE_DATA_ERROR);
        } else {
            ChatPromptTemplatePo existing = chatPromptTemplateDao.selectById(id);
            Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
            // 更新：直接更新
            entity.setUpdateTime(Moments.now());
            int success = chatPromptTemplateDao.update(entity);
            Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        }

        return entity.getId();
    }

    @Override
    public int updateStatus(Long id, UpdateStatusDto dto) {
        ResourceStatus.validate(dto.getStatus());
        ChatPromptTemplatePo existing = chatPromptTemplateDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);

        ChatPromptTemplatePo entity = new ChatPromptTemplatePo();
        entity.setId(id);
        entity.setStatus(dto.getStatus());
        entity.setUpdateTime(Moments.now());
        int success = chatPromptTemplateDao.update(entity);
        Asserts.greaterThan(success, 0, SystemErrorCode.UPDATE_DATA_ERROR, id);
        return success;
    }

    @Override
    public int delete(Long id) {
        ChatPromptTemplatePo existing = chatPromptTemplateDao.selectById(id);
        Asserts.isTrue(Objects.nonNull(existing), SystemErrorCode.DATA_NOT_EXISTS, id);
        return chatPromptTemplateDao.delete(id);
    }
}
