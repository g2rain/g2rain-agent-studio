package com.g2rain.agent.studio.service.impl;

import com.g2rain.agent.studio.dao.AgentProfileDao;
import com.g2rain.agent.studio.dao.SkillProfileDao;
import com.g2rain.agent.studio.dao.SkillRevisionDao;
import com.g2rain.agent.studio.dao.SkillToolBindingDao;
import com.g2rain.agent.studio.dao.po.AgentProfilePo;
import com.g2rain.agent.studio.dao.po.SkillProfilePo;
import com.g2rain.agent.studio.dao.po.SkillRevisionPo;
import com.g2rain.agent.studio.dao.po.SkillToolBindingPo;
import com.g2rain.agent.studio.dto.SkillProfileSelectDto;
import com.g2rain.agent.studio.dto.SkillToolBindingSelectDto;
import com.g2rain.agent.studio.enums.ResourceStatus;
import com.g2rain.agent.studio.enums.RevisionStatus;
import com.g2rain.agent.studio.enums.StudioErrorCode;
import com.g2rain.agent.studio.vo.AgentBundleVo;
import com.g2rain.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 技能包按修订返回。
 *
 * @author alpha
 * @since 2026/11/25
 */
@ExtendWith(MockitoExtension.class)
class AgentProfileCapabilityBundleTest {

    @Mock
    private AgentProfileDao agentProfileDao;

    @Mock
    private SkillProfileDao skillProfileDao;

    @Mock
    private SkillToolBindingDao skillToolBindingDao;

    @Mock
    private SkillRevisionDao skillRevisionDao;

    @InjectMocks
    private AgentProfileServiceImpl service;

    @Test
    void selectCapabilityBundle_withRevision_replacesOnlyThatSkill() {
        SkillProfilePo skillA = skill(10L, 1L, "cms.a", "技能A", "手册A");
        SkillProfilePo skillB = skill(11L, 1L, "cms.b", "技能B", "手册B");
        stubActiveSkills(skillA, skillB);
        SkillToolBindingPo bindingA = binding(10L, "tool.a");
        SkillToolBindingPo bindingB = binding(11L, "tool.b");
        when(skillToolBindingDao.selectListWithoutIsolation(any(SkillToolBindingSelectDto.class)))
            .thenReturn(List.of(bindingA, bindingB));

        SkillRevisionPo revision = new SkillRevisionPo();
        revision.setId(900L);
        revision.setOrganId(3L);
        revision.setSkillId(10L);
        revision.setSkillName("修订名");
        revision.setSemanticDesc("修订语义");
        revision.setSkillManual("修订手册");
        revision.setSkillContract("{\"version\":\"2.0\"}");
        revision.setToolNames("[\"tool.x\"]");
        revision.setStatus(RevisionStatus.DRAFT.name());
        when(skillRevisionDao.selectById(900L)).thenReturn(revision);

        AgentProfilePo agent = new AgentProfilePo();
        agent.setId(1L);
        agent.setOrganId(3L);
        when(agentProfileDao.selectById(1L)).thenReturn(agent);
        when(skillProfileDao.selectByIdWithoutIsolation(10L)).thenReturn(skillA);

        AgentBundleVo bundle = service.selectCapabilityBundle(1L, 900L);

        assertThat(bundle.getSkills()).hasSize(2);
        AgentBundleVo.SkillBriefVo overridden = bundle.getSkills().stream()
            .filter(s -> "cms.a".equals(s.getSkillKey()))
            .findFirst()
            .orElseThrow();
        assertThat(overridden.getSkillName()).isEqualTo("修订名");
        assertThat(overridden.getSkillManual()).isEqualTo("修订手册");
        assertThat(overridden.getSkillContract()).isEqualTo("{\"version\":\"2.0\"}");
        assertThat(overridden.getTools()).extracting(AgentBundleVo.ToolBriefVo::getToolName)
            .containsExactly("tool.x");

        AgentBundleVo.SkillBriefVo other = bundle.getSkills().stream()
            .filter(s -> "cms.b".equals(s.getSkillKey()))
            .findFirst()
            .orElseThrow();
        assertThat(other.getSkillName()).isEqualTo("技能B");
        assertThat(other.getSkillManual()).isEqualTo("手册B");
        assertThat(other.getTools()).extracting(AgentBundleVo.ToolBriefVo::getToolName)
            .containsExactly("tool.b");
    }

    @Test
    void selectCapabilityBundle_revisionOfOtherAgent_rejected() {
        SkillProfilePo skillA = skill(10L, 1L, "cms.a", "技能A", "手册A");
        stubActiveSkills(skillA);
        when(skillToolBindingDao.selectListWithoutIsolation(any(SkillToolBindingSelectDto.class)))
            .thenReturn(List.of());

        SkillRevisionPo revision = new SkillRevisionPo();
        revision.setId(901L);
        revision.setOrganId(3L);
        revision.setSkillId(20L);
        when(skillRevisionDao.selectById(901L)).thenReturn(revision);

        AgentProfilePo agent = new AgentProfilePo();
        agent.setId(1L);
        agent.setOrganId(3L);
        when(agentProfileDao.selectById(1L)).thenReturn(agent);

        SkillProfilePo otherSkill = skill(20L, 2L, "cms.other", "其他", "手册");
        when(skillProfileDao.selectByIdWithoutIsolation(20L)).thenReturn(otherSkill);

        assertThatThrownBy(() -> service.selectCapabilityBundle(1L, 901L))
            .isInstanceOf(BusinessException.class)
            .extracting(ex -> ((BusinessException) ex).getErrorCode())
            .isEqualTo(StudioErrorCode.REVISION_AGENT_MISMATCH.code());
    }

    @Test
    void selectCapabilityBundle_withoutRevision_unchanged() {
        SkillProfilePo skillA = skill(10L, 1L, "cms.a", "技能A", "手册A");
        stubActiveSkills(skillA);
        when(skillToolBindingDao.selectListWithoutIsolation(any(SkillToolBindingSelectDto.class)))
            .thenReturn(List.of(binding(10L, "tool.a")));

        AgentBundleVo bundle = service.selectCapabilityBundle(1L, null);

        assertThat(bundle.getSkills()).hasSize(1);
        assertThat(bundle.getSkills().getFirst().getSkillName()).isEqualTo("技能A");
        assertThat(bundle.getSkills().getFirst().getTools()).extracting(AgentBundleVo.ToolBriefVo::getToolName)
            .containsExactly("tool.a");
    }

    private void stubActiveSkills(SkillProfilePo... skills) {
        when(skillProfileDao.selectList(any(SkillProfileSelectDto.class))).thenAnswer(invocation -> {
            SkillProfileSelectDto select = invocation.getArgument(0);
            if (ResourceStatus.ACTIVE.name().equals(select.getStatus())
                && "CUSTOM".equals(select.getResourceType())) {
                return List.of();
            }
            return List.of();
        });
        when(skillProfileDao.selectListWithoutIsolation(any(SkillProfileSelectDto.class)))
            .thenReturn(List.of(skills));
    }

    private static SkillProfilePo skill(Long id, Long agentId, String key, String name, String manual) {
        SkillProfilePo skill = new SkillProfilePo();
        skill.setId(id);
        skill.setOrganId(3L);
        skill.setAgentId(agentId);
        skill.setSkillKey(key);
        skill.setSkillName(name);
        skill.setSkillManual(manual);
        skill.setSemanticDesc("语义");
        skill.setSkillContract("{\"version\":\"1.0\"}");
        skill.setStatus(ResourceStatus.ACTIVE.name());
        skill.setResourceType("PLATFORM");
        return skill;
    }

    private static SkillToolBindingPo binding(Long skillId, String toolName) {
        SkillToolBindingPo binding = new SkillToolBindingPo();
        binding.setSkillId(skillId);
        binding.setToolName(toolName);
        return binding;
    }
}
