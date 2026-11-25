package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.dao.SkillProfileDao;
import com.g2rain.agent.studio.dao.SkillRevisionDao;
import com.g2rain.agent.studio.dao.SkillToolBindingDao;
import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dao.po.SkillRevisionPo;
import com.g2rain.agent.studio.dao.po.SkillToolBindingPo;
import com.g2rain.agent.studio.dto.CreateSkillRevisionDraftDto;
import com.g2rain.agent.studio.dto.PublishSkillRevisionDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.enums.RevisionOrigin;
import com.g2rain.agent.studio.enums.RevisionStatus;
import com.g2rain.agent.studio.enums.StudioErrorCode;
import com.g2rain.common.exception.BusinessException;
import com.g2rain.common.id.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 技能修订：保存留痕、草稿、发布、丢弃、回滚。
 *
 * @author alpha
 * @since 2026/11/25
 */
@ExtendWith(MockitoExtension.class)
class SkillRevisionServiceImplTest {

    @Mock
    private SkillRevisionDao skillRevisionDao;

    @Mock
    private SkillProfileDao skillProfileDao;

    @Mock
    private SkillToolBindingDao skillToolBindingDao;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private SkillRevisionServiceImpl service;

    @BeforeEach
    void setUp() {
        service.setIdGenerator(idGenerator);
    }

    @Test
    void recordPublishedSnapshot_supersedesAndInsertsManual() {
        SkillProfilePo skill = skill(10L, 3L);
        when(skillProfileDao.selectByIdForUpdate(10L)).thenReturn(skill);
        when(skillRevisionDao.supersedePublished(10L)).thenReturn(1);
        when(skillRevisionDao.selectMaxRevisionNo(10L)).thenReturn(1);
        when(skillToolBindingDao.selectList(any(SkillToolBindingSelectDto.class)))
            .thenReturn(List.of(binding(10L, "tool.a")));
        when(idGenerator.generateId()).thenReturn(501L);
        when(skillRevisionDao.insert(any(SkillRevisionPo.class))).thenReturn(1);

        service.recordPublishedSnapshot(10L);

        ArgumentCaptor<SkillRevisionPo> captor = ArgumentCaptor.forClass(SkillRevisionPo.class);
        verify(skillRevisionDao).supersedePublished(10L);
        verify(skillRevisionDao).insert(captor.capture());
        SkillRevisionPo row = captor.getValue();
        assertThat(row.getId()).isEqualTo(501L);
        assertThat(row.getRevisionNo()).isEqualTo(2);
        assertThat(row.getStatus()).isEqualTo(RevisionStatus.PUBLISHED.name());
        assertThat(row.getOrigin()).isEqualTo(RevisionOrigin.MANUAL.name());
        assertThat(row.getSkillName()).isEqualTo("技能A");
        assertThat(row.getToolNames()).contains("tool.a");
    }

    @Test
    void createDraft_overridesPublishedFields() {
        SkillProfilePo skill = skill(10L, 3L);
        SkillRevisionPo published = published(200L, 10L, 1, "[\"tool.a\"]");
        when(skillProfileDao.selectById(10L)).thenReturn(skill);
        when(skillRevisionDao.selectPublished(10L)).thenReturn(published);
        when(idGenerator.generateId()).thenReturn(601L);
        when(skillRevisionDao.insert(any(SkillRevisionPo.class))).thenReturn(1);

        CreateSkillRevisionDraftDto dto = new CreateSkillRevisionDraftDto();
        dto.setSkillId(10L);
        dto.setSkillName("草稿名");
        dto.setToolNames(List.of("tool.b"));
        dto.setNote("候选");

        Long draftId = service.createDraft(dto);

        assertThat(draftId).isEqualTo(601L);
        ArgumentCaptor<SkillRevisionPo> captor = ArgumentCaptor.forClass(SkillRevisionPo.class);
        verify(skillRevisionDao).insert(captor.capture());
        SkillRevisionPo draft = captor.getValue();
        assertThat(draft.getStatus()).isEqualTo(RevisionStatus.DRAFT.name());
        assertThat(draft.getOrigin()).isEqualTo(RevisionOrigin.CANDIDATE.name());
        assertThat(draft.getRevisionNo()).isZero();
        assertThat(draft.getBaseRevisionNo()).isEqualTo(1);
        assertThat(draft.getSkillName()).isEqualTo("草稿名");
        assertThat(draft.getSemanticDesc()).isEqualTo(published.getSemanticDesc());
        assertThat(draft.getToolNames()).contains("tool.b");
        assertThat(draft.getNote()).isEqualTo("候选");
    }

    @Test
    void publish_expectedRevisionMismatch_rejected() {
        SkillRevisionPo draft = draft(601L, 10L);
        when(skillRevisionDao.selectById(601L)).thenReturn(draft);
        when(skillProfileDao.selectByIdForUpdate(10L)).thenReturn(skill(10L, 3L));
        when(skillRevisionDao.selectPublished(10L)).thenReturn(published(200L, 10L, 2, "[]"));

        PublishSkillRevisionDto dto = new PublishSkillRevisionDto();
        dto.setExpectedRevisionNo(1);

        assertThatThrownBy(() -> service.publish(601L, dto))
            .isInstanceOf(BusinessException.class)
            .extracting(ex -> ((BusinessException) ex).getErrorCode())
            .isEqualTo(StudioErrorCode.SKILL_MODIFIED.code());
    }

    @Test
    void publish_writesSnapshotWithoutManualInsert() {
        SkillRevisionPo draft = draft(601L, 10L);
        draft.setSkillName("发布名");
        draft.setSemanticDesc("发布语义");
        draft.setSkillManual("发布手册");
        draft.setSkillContract("{\"version\":\"1.0\"}");
        draft.setToolNames("[\"tool.b\"]");
        SkillProfilePo locked = skill(10L, 3L);
        SkillRevisionPo published = published(200L, 10L, 1, "[\"tool.a\"]");
        SkillToolBindingPo oldBinding = binding(10L, "tool.a");
        oldBinding.setId(88L);

        when(skillRevisionDao.selectById(601L)).thenReturn(draft);
        when(skillProfileDao.selectByIdForUpdate(10L)).thenReturn(locked);
        when(skillRevisionDao.selectPublished(10L)).thenReturn(published);
        when(skillProfileDao.update(any(SkillProfilePo.class))).thenReturn(1);
        when(skillToolBindingDao.selectList(any(SkillToolBindingSelectDto.class))).thenReturn(List.of(oldBinding));
        when(skillToolBindingDao.delete(88L)).thenReturn(1);
        when(idGenerator.generateId()).thenReturn(701L);
        when(skillToolBindingDao.insert(any(SkillToolBindingPo.class))).thenReturn(1);
        when(skillRevisionDao.updateStatus(eq(200L), eq(RevisionStatus.PUBLISHED.name()),
            eq(RevisionStatus.SUPERSEDED.name()), isNull())).thenReturn(1);
        when(skillRevisionDao.selectMaxRevisionNo(10L)).thenReturn(1);
        when(skillRevisionDao.updateStatus(eq(601L), eq(RevisionStatus.DRAFT.name()),
            eq(RevisionStatus.PUBLISHED.name()), eq(2))).thenReturn(1);

        PublishSkillRevisionDto dto = new PublishSkillRevisionDto();
        dto.setExpectedRevisionNo(1);
        service.publish(601L, dto);

        ArgumentCaptor<SkillProfilePo> skillCaptor = ArgumentCaptor.forClass(SkillProfilePo.class);
        verify(skillProfileDao).update(skillCaptor.capture());
        assertThat(skillCaptor.getValue().getSkillName()).isEqualTo("发布名");
        assertThat(skillCaptor.getValue().getSkillManual()).isEqualTo("发布手册");

        ArgumentCaptor<SkillToolBindingPo> bindingCaptor = ArgumentCaptor.forClass(SkillToolBindingPo.class);
        verify(skillToolBindingDao).delete(88L);
        verify(skillToolBindingDao).insert(bindingCaptor.capture());
        assertThat(bindingCaptor.getValue().getToolName()).isEqualTo("tool.b");

        verify(skillRevisionDao).updateStatus(601L, RevisionStatus.DRAFT.name(),
            RevisionStatus.PUBLISHED.name(), 2);
        verify(skillRevisionDao, never()).insert(any(SkillRevisionPo.class));
        verify(skillRevisionDao, never()).supersedePublished(any());
    }

    @Test
    void publish_nonDraft_rejected() {
        SkillRevisionPo published = published(200L, 10L, 1, "[]");
        when(skillRevisionDao.selectById(200L)).thenReturn(published);

        PublishSkillRevisionDto dto = new PublishSkillRevisionDto();
        dto.setExpectedRevisionNo(1);

        assertThatThrownBy(() -> service.publish(200L, dto))
            .isInstanceOf(BusinessException.class)
            .extracting(ex -> ((BusinessException) ex).getErrorCode())
            .isEqualTo(StudioErrorCode.REVISION_STATUS_INVALID.code());
    }

    @Test
    void discard_marksDiscarded() {
        when(skillRevisionDao.selectById(601L)).thenReturn(draft(601L, 10L));
        when(skillRevisionDao.updateStatus(601L, RevisionStatus.DRAFT.name(),
            RevisionStatus.DISCARDED.name(), null)).thenReturn(1);

        service.discard(601L);

        verify(skillRevisionDao).updateStatus(601L, RevisionStatus.DRAFT.name(),
            RevisionStatus.DISCARDED.name(), null);
    }

    @Test
    void rollback_insertsRollbackPublished() {
        SkillRevisionPo source = published(200L, 10L, 1, "[\"tool.a\"]");
        source.setStatus(RevisionStatus.SUPERSEDED.name());
        source.setSkillName("历史名");
        SkillProfilePo locked = skill(10L, 3L);
        when(skillRevisionDao.selectById(200L)).thenReturn(source);
        when(skillProfileDao.selectByIdForUpdate(10L)).thenReturn(locked);
        when(skillProfileDao.update(any(SkillProfilePo.class))).thenReturn(1);
        when(skillToolBindingDao.selectList(any(SkillToolBindingSelectDto.class))).thenReturn(List.of());
        when(idGenerator.generateId()).thenReturn(801L, 802L);
        when(skillToolBindingDao.insert(any(SkillToolBindingPo.class))).thenReturn(1);
        when(skillRevisionDao.supersedePublished(10L)).thenReturn(1);
        when(skillRevisionDao.selectMaxRevisionNo(10L)).thenReturn(2);
        when(skillRevisionDao.insert(any(SkillRevisionPo.class))).thenReturn(1);

        service.rollback(200L);

        ArgumentCaptor<SkillRevisionPo> captor = ArgumentCaptor.forClass(SkillRevisionPo.class);
        verify(skillRevisionDao).insert(captor.capture());
        SkillRevisionPo row = captor.getValue();
        assertThat(row.getOrigin()).isEqualTo(RevisionOrigin.ROLLBACK.name());
        assertThat(row.getStatus()).isEqualTo(RevisionStatus.PUBLISHED.name());
        assertThat(row.getRevisionNo()).isEqualTo(3);
        assertThat(row.getSkillName()).isEqualTo("历史名");
        assertThat(row.getToolNames()).contains("tool.a");
    }

    @Test
    void deleteBySkillId_logicallyDeletesRevisions() {
        when(skillRevisionDao.deleteBySkillId(10L)).thenReturn(2);

        service.deleteBySkillId(10L);

        verify(skillRevisionDao).deleteBySkillId(10L);
    }

    private static SkillProfilePo skill(Long id, Long organId) {
        SkillProfilePo skill = new SkillProfilePo();
        skill.setId(id);
        skill.setOrganId(organId);
        skill.setAgentId(1L);
        skill.setSkillKey("cms.demo");
        skill.setSkillName("技能A");
        skill.setSemanticDesc("语义");
        skill.setSkillManual("手册");
        skill.setSkillContract("{\"version\":\"1.0\"}");
        return skill;
    }

    private static SkillRevisionPo published(Long id, Long skillId, int revisionNo, String toolNames) {
        SkillRevisionPo row = new SkillRevisionPo();
        row.setId(id);
        row.setOrganId(3L);
        row.setSkillId(skillId);
        row.setRevisionNo(revisionNo);
        row.setSkillName("技能A");
        row.setSemanticDesc("语义");
        row.setSkillManual("手册");
        row.setSkillContract("{\"version\":\"1.0\"}");
        row.setToolNames(toolNames);
        row.setStatus(RevisionStatus.PUBLISHED.name());
        row.setOrigin(RevisionOrigin.MANUAL.name());
        return row;
    }

    private static SkillRevisionPo draft(Long id, Long skillId) {
        SkillRevisionPo row = published(id, skillId, 0, "[]");
        row.setStatus(RevisionStatus.DRAFT.name());
        row.setOrigin(RevisionOrigin.CANDIDATE.name());
        row.setBaseRevisionNo(1);
        return row;
    }

    private static SkillToolBindingPo binding(Long skillId, String toolName) {
        SkillToolBindingPo binding = new SkillToolBindingPo();
        binding.setSkillId(skillId);
        binding.setToolName(toolName);
        binding.setOrganId(3L);
        return binding;
    }
}
