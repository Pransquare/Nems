package com.pransquare.nems.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.TdsConfig;
import com.pransquare.nems.services.TdsConfigService;

@RestController
@RequestMapping("/api/tds-config")
public class TdsConfigController {

    private TdsConfigService tdsConfigService;

    public TdsConfigController(TdsConfigService tdsConfigService) {
		this.tdsConfigService = tdsConfigService;
	}

	@GetMapping
    public ResponseEntity<List<TdsConfig>> getAllTdsConfigs() {
        return ResponseEntity.ok(tdsConfigService.getAllTdsConfigs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TdsConfig> getTdsConfigById(@PathVariable int id) {
        Optional<TdsConfig> tdsConfig = tdsConfigService.getTdsConfigById(id);
        return tdsConfig.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TdsConfig> createTdsConfig(@RequestBody TdsConfig tdsConfig) {
        return ResponseEntity.ok(tdsConfigService.createTdsConfig(tdsConfig));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TdsConfig> updateTdsConfig(@PathVariable int id, @RequestBody TdsConfig tdsConfig) {
        return ResponseEntity.ok(tdsConfigService.updateTdsConfig(id, tdsConfig));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTdsConfig(@PathVariable int id) {
        tdsConfigService.deleteTdsConfig(id);
        return ResponseEntity.noContent().build();
    }
}
