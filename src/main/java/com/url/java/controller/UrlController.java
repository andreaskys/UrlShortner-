package com.url.java.controller;

import com.url.java.config.RabbitConfig;
import com.url.java.dto.ClickEventDTO;
import com.url.java.model.UrlEntity;
import com.url.java.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.url.java.repository.UrlRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UrlController{

    private final RabbitTemplate rabbitTemplate;
    private final UrlService service;
    private final UrlRepository repository;

    public UrlController(UrlService service, RabbitTemplate rabbitTemplate, UrlRepository repository){
        this.service = service;
        this.rabbitTemplate =  rabbitTemplate;
        this.repository = repository;
    }

      @PostMapping("/shorten")
      public ResponseEntity<String> shorten(@RequestBody String originalUrl){
        String code = service.shortenUrl(originalUrl);
        return ResponseEntity.ok("http://localhost:8080/" + code);
      }

      @GetMapping("/{shortCode}")
      public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletRequest request){
        String originalUrl = service.getOriginalUrl(shortCode);

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        ClickEventDTO event = new ClickEventDTO(shortCode, ip, userAgent, LocalDateTime.now());
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, event);

        return ResponseEntity.status(HttpStatus.FOUND)
              .location(URI.create(originalUrl))
              .build();
  }

  @GetMapping("/api/stats")
    public ResponseEntity<List<UrlEntity>> getStats(){
        return ResponseEntity.ok(repository.findTop10ByOrderByCreatedAtDesc());
  }
}
