package com.url.java.task;

import com.url.java.repository.UrlRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class AnalyticsSyncTask{

  private final UrlRepository repository;
  private final StringRedisTemplate redisTemplate;

  public AnalyticsSyncTask(UrlRepository repository, StringRedisTemplate redisTemplate){
    this.repository = repository;
    this.redisTemplate = redisTemplate;
  }

  @Scheduled(fixedDelay = 10000)
  @Transactional
  public void syncClicksToDatabase(){

    Set<String> changedShortCodes = redisTemplate.opsForSet().members("changed_urls");
    if(changedShortCodes == null || changedShortCodes.isEmpty()){
      return;
    } 

    for(String shortCode: changedShortCodes){
      String clickKey = "clicks:" + shortCode;

      String countStr = redisTemplate.opsForValue().getAndDelete(clickKey);

      if(countStr != null){
        Long clicksToAdd = Long.parseLong(countStr);
        repository.incrementClicks(shortCode, clicksToAdd);
      }
    }

    redisTemplate.opsForSet().remove("changed_urls", changedShortCodes.toArray());
    System.out.println("Sincronizados " + changedShortCodes.size() + " Urls com banco de Dados");


  }

}
