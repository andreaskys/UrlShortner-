package com.url.java.service;

import com.url.java.model.UrlEntity;
import com.url.java.repository.UrlRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.time.Duration;

@Service
public class UrlService {
    private final StringRedisTemplate redisTemplate;
    private final UrlRepository repository;

  private static final Duration CACHE_TTL = Duration.ofHours(24);
  

  public UrlService(UrlRepository repository, StringRedisTemplate redisTemplate){
    this.repository = repository;
    this.redisTemplate = redisTemplate;
  }

  public String shortenUrl(String originalUrl){
    String shortCode = generateShortCode();
    UrlEntity url = new UrlEntity();
    url.setOriginalUrl(originalUrl);
    url.setShortCode(shortCode);
    repository.save(url);
    return shortCode;
  }

  public String getOriginalUrl(String shortCode){
    String cachedUrl = redisTemplate.opsForValue().get(shortCode);

    if(cachedUrl != null){
      return cachedUrl;
    }
    System.out.println("Cache Miss - Searching DataBase.");
    String originalUrl = repository.findByShortCode(shortCode)
      .map(UrlEntity::getOriginalUrl)
      .orElseThrow(() -> new RuntimeException("URL not found"));

    redisTemplate.opsForValue().set(shortCode, originalUrl, CACHE_TTL);
    return originalUrl;
  }

  private String generateShortCode(){
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder code = new StringBuilder();
    Random random = new Random();

    for(int i = 0; i < 6; i++){
      code.append(characters.charAt(random.nextInt(characters.length())));
    }
    return code.toString();

  }

  public void incrementClick(String shortCode){
    redisTemplate.opsForValue().increment("Clicks: " + shortCode);
    //redisTemplate.opsForValue().add("changed_urls", shortCode);
  }

}
