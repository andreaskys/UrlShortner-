package com.url.java.repository;

import com.url.java.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long>{
  Optional<UrlEntity> findByShortCode(String shortcode);


  @Modifying
  @Transactional
  @Query("UPDATE UrlEntity u SET u.clickCount = u.clickCount + :clicks WHERE u.shortCode = :shortCode")
  void incrementClicks(String shortCode, Long clicks);
}
