package com.pransquare.nems.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.TdsConfig;
import com.pransquare.nems.repositories.TdsConfigRepository;

@Service
public class TdsConfigService {

    private TdsConfigRepository tdsConfigRepository;
    
    public TdsConfigService(TdsConfigRepository tdsConfigRepository) {
		this.tdsConfigRepository = tdsConfigRepository;
	}

	public List<TdsConfig> getAllTdsConfigs() {
        return tdsConfigRepository.findAll();
    }

    public Optional<TdsConfig> getTdsConfigById(int id) {
        return tdsConfigRepository.findById(id);
    }

    public TdsConfig createTdsConfig(TdsConfig tdsConfig) {
        return tdsConfigRepository.save(tdsConfig);
    }

    public TdsConfig updateTdsConfig(int id, TdsConfig updatedTdsConfig) {
        return tdsConfigRepository.findById(id).map(tdsConfig -> {
            tdsConfig.setCode(updatedTdsConfig.getCode());
            tdsConfig.setResimeCode(updatedTdsConfig.getResimeCode());
            tdsConfig.setAcademicYear(updatedTdsConfig.getAcademicYear());
            tdsConfig.setCreatedBy(updatedTdsConfig.getCreatedBy());
            tdsConfig.setCreatedDate(updatedTdsConfig.getCreatedDate());
            tdsConfig.setModifiedBy(updatedTdsConfig.getModifiedBy());
            tdsConfig.setModifiedDate(updatedTdsConfig.getModifiedDate());
            tdsConfig.setStatus(updatedTdsConfig.getStatus());
            return tdsConfigRepository.save(tdsConfig);
        }).orElseThrow(() -> new RuntimeException("TdsConfig not found"));
    }

    public void deleteTdsConfig(int id) {
        tdsConfigRepository.deleteById(id);
    }
}
