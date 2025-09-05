package com.pransquare.nems.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.GroupSubgroupConfigEntity;
import com.pransquare.nems.entities.GroupSubgroupGoalEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.SaveGoalsForGroupModel;
import com.pransquare.nems.models.SearchGoalsModel;
import com.pransquare.nems.repositories.GroupSubgroupConfigRepository;
import com.pransquare.nems.repositories.GroupSubgroupGoalRepository;
import com.pransquare.nems.utils.IntegerUtils;
import com.pransquare.nems.utils.StringUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class GoalsConfigService {

	private final GroupSubgroupConfigRepository groupSubgroupConfigRepository;

	private final GroupSubgroupGoalRepository groupSubgroupGoalRepository;

	public GoalsConfigService(GroupSubgroupConfigRepository groupSubgroupConfigRepository,
			GroupSubgroupGoalRepository groupSubgroupGoalRepository) {
		this.groupSubgroupConfigRepository = groupSubgroupConfigRepository;
		this.groupSubgroupGoalRepository = groupSubgroupGoalRepository;
	}

	public Map<String, Object> saveGoalsForGroup(SaveGoalsForGroupModel saveGoalsForGroupModel) {
		Map<String, Object> returnResponse = new HashMap<>();
		List<String> goals = saveGoalsForGroupModel.getGoals();
		if (IntegerUtils.isNotNull(saveGoalsForGroupModel.getConfigId())) {
			Optional<GroupSubgroupConfigEntity> configUpdate = groupSubgroupConfigRepository
					.findById(saveGoalsForGroupModel.getConfigId());
			if (configUpdate.isPresent()) {
				GroupSubgroupConfigEntity config = configUpdate.get();
				config.setStatus(saveGoalsForGroupModel.getStatus());
				config.setModifiedBy(saveGoalsForGroupModel.getCreatedBy());
				config.setModifiedDate(LocalDateTime.now());
				config.setGroup(saveGoalsForGroupModel.getGroup());
				config.setSubGroup(saveGoalsForGroupModel.getSubGroup());
				groupSubgroupConfigRepository.save(config);
				List<GroupSubgroupGoalEntity> existGoalEntity = config.getGroupSubgroupGoalEntity();
				List<String> existGoals = existGoalEntity.stream()
						.map(GroupSubgroupGoalEntity::getGoal)
						.toList();
				List<GroupSubgroupGoalEntity> goalsEntity = groupSubgroupGoalRepository
						.findByGroupSubgroupConfigId(saveGoalsForGroupModel.getConfigId());
				for (GroupSubgroupGoalEntity goalent : goalsEntity) {
					goalent.setGroupSubgroupConfigHistoryId(config.getGroupSubgroupConfigId());
					goalent.setGroupSubgroupConfigId(null);
					// goalent.setGoal(goal);
					goalent.setModifiedBy(saveGoalsForGroupModel.getCreatedBy());
					goalent.setModifiedDate(LocalDateTime.now());
					groupSubgroupGoalRepository.save(goalent);

				}
				for (String goal : goals) {
					// if(!existGoals.contains(goal));
					GroupSubgroupGoalEntity goalEntity = new GroupSubgroupGoalEntity();
					goalEntity.setGroupSubgroupConfigId(config.getGroupSubgroupConfigId());
					goalEntity.setGoal(goal);
					goalEntity.setCreatedBy(saveGoalsForGroupModel.getCreatedBy());
					goalEntity.setCreatedDate(LocalDateTime.now());
					groupSubgroupGoalRepository.save(goalEntity);

				}
				// for(String goal:existGoals) {
				// if(!goals.contains(goal));
				// GroupSubgroupGoalEntity goalEntity
				// =groupSubgroupGoalRepository.findByGoalAndGroupSubgroupConfigId(goal,saveGoalsForGroupModel.getConfigId());
				// goalEntity.setGroupSubgroupConfigHistoryId(config.getGroupSubgroupConfigId());
				// goalEntity.setGroupSubgroupConfigId(null);
				// goalEntity.setGoal(goal);
				// goalEntity.setModifiedBy(saveGoalsForGroupModel.getCreatedBy());
				// goalEntity.setModifiedDate(LocalDateTime.now());
				// groupSubgroupGoalRepository.save(goalEntity);
				//
				// }
			}
			returnResponse.put("response", "Data updated successfully");
		} else {
			GroupSubgroupConfigEntity configEntity;
			if (StringUtil.isNotNull(saveGoalsForGroupModel.getGroup())
					&& StringUtil.isNotNull(saveGoalsForGroupModel.getSubGroup())) {
				configEntity = groupSubgroupConfigRepository.findByGroupAndSubGroupAndStatus(
						saveGoalsForGroupModel.getGroup(), saveGoalsForGroupModel.getSubGroup(), "108");
				if (configEntity != null) {
					saveGoalsForGroupModel.setConfigId(configEntity.getGroupSubgroupConfigId());
					return saveGoalsForGroup(saveGoalsForGroupModel);
				} else {
					configEntity = new GroupSubgroupConfigEntity();
				}
			} else {
				throw new IllegalArgumentException("Group and Subgroup cannot be null");
			}
			configEntity.setCreatedBy(saveGoalsForGroupModel.getCreatedBy());
			configEntity.setCreatedDate(LocalDateTime.now());
			configEntity.setGroup(saveGoalsForGroupModel.getGroup());
			configEntity.setSubGroup(saveGoalsForGroupModel.getSubGroup());
			configEntity.setStatus(saveGoalsForGroupModel.getStatus());
			configEntity = groupSubgroupConfigRepository.save(configEntity);

			for (String goal : goals) {
				GroupSubgroupGoalEntity goalEntity = new GroupSubgroupGoalEntity();
				goalEntity.setGroupSubgroupConfigId(configEntity.getGroupSubgroupConfigId());
				goalEntity.setGoal(goal);
				goalEntity.setCreatedBy(saveGoalsForGroupModel.getCreatedBy());
				goalEntity.setCreatedDate(LocalDateTime.now());
				groupSubgroupGoalRepository.save(goalEntity);

			}
			returnResponse.put("response", "Data saved successfully");
		}
		return returnResponse;
	}

	public Page<GroupSubgroupConfigEntity> searchGoalsByGroupAndSubgroup(SearchGoalsModel searchGoalsModel) {

		try {
			Specification<GroupSubgroupConfigEntity> spec = Specification.where(null);

			if (Boolean.TRUE.equals(StringUtil.isNotNull(searchGoalsModel.getGroup()))) {
				spec = spec.and(hasGroup(searchGoalsModel.getGroup()));
			}
			if (Boolean.TRUE.equals(StringUtil.isNotNull(searchGoalsModel.getSubGroup()))) {
				spec = spec.and(hasSubGroup(searchGoalsModel.getSubGroup()));
			}
			spec = spec.and(hasStatus("108"));
			Pageable pageable = PageRequest.of(searchGoalsModel.getPage(), searchGoalsModel.getSize());

			return groupSubgroupConfigRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}

	public static Specification<GroupSubgroupConfigEntity> hasStatus(String status) {
		return (Root<GroupSubgroupConfigEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) -> criteriaBuilder
						.equal(root.get("status"), status);
	}

	public static Specification<GroupSubgroupConfigEntity> hasGroup(String group) {
		return (Root<GroupSubgroupConfigEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) -> criteriaBuilder
						.equal(root.get("group"), group);
	}

	public static Specification<GroupSubgroupConfigEntity> hasSubGroup(String subGroup) {
		return (Root<GroupSubgroupConfigEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) -> criteriaBuilder
						.equal(root.get("subGroup"), subGroup);
	}

}
