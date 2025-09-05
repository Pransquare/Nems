package com.pransquare.nems.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.NewsEntity;
import com.pransquare.nems.services.NewsService;

@RestController
@RequestMapping("/Pransquare/nems/news")
public class NewsController {

	private final NewsService newsService;

	public NewsController(NewsService newsService) {
		this.newsService = newsService;
	}

	@PostMapping
	public ResponseEntity<NewsEntity> create(@RequestBody NewsEntity newsEntity) {
		return ResponseEntity.ok(newsService.create(newsEntity));
	}

	@PutMapping("/{newsId}")
	public ResponseEntity<NewsEntity> update(@PathVariable Long newsId, @RequestBody NewsEntity updatedNewsEntity) {
		return ResponseEntity.ok(newsService.update(newsId, updatedNewsEntity));
	}

	@DeleteMapping("/{newsId}")
	public ResponseEntity<Void> delete(@PathVariable Long newsId) {
		newsService.delete(newsId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<NewsEntity>> getAll() {
		return ResponseEntity.ok(newsService.getAll());
	}

	@GetMapping("/{newsId}")
	public ResponseEntity<NewsEntity> getById(@PathVariable Long newsId) {
		return newsService.getById(newsId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}