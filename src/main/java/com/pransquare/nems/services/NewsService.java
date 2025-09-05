package com.pransquare.nems.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.NewsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.repositories.NewsRepository;

@Service
public class NewsService {


    private NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public NewsEntity create(NewsEntity newsEntity) {
        return newsRepository.save(newsEntity);
    }

    public NewsEntity update(Long newsId, NewsEntity updatedNewsEntity) {
        Optional<NewsEntity> existingNews = newsRepository.findById(newsId);
        if (existingNews.isPresent()) {
            updatedNewsEntity.setNewsId(newsId);
            return newsRepository.save(updatedNewsEntity);
        } else {
            throw new ResourceNotFoundException("News not found with id: " + newsId);
        }
    }

    public void delete(Long newsId) {
        Optional<NewsEntity> existingNews = newsRepository.findById(newsId);
        if (existingNews.isPresent()) {
            newsRepository.deleteById(newsId);
        } else {
            throw new ResourceNotFoundException("News not found with id: " + newsId);
        }
    }

    public List<NewsEntity> getAll() {
        return newsRepository.findAll();
    }

    public Optional<NewsEntity> getById(Long newsId) {
        return newsRepository.findById(newsId);
    }
}