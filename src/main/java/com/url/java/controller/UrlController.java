package com.url.java.controller;

import com.url.java.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
public class UrlController{

  private final UrlService service;

  public UrlController(UrlService service){
    this.service = service;
  }

  @PostMapping("/shorten")
  public ResponseEntity<String> shorten(@RequestBody String originalUrl){
    String code = service.shortenUrl(originalUrl);
    return ResponseEntity.ok("http://localhost:8080/" + code);
  }

  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirect(@PathVariable String shortCode){
    String originalUrl = service.getOriginalUrl(shortCode);
    service.incrementClick(shortCode);
    return ResponseEntity.status(HttpStatus.FOUND)
      .location(URI.create(originalUrl))
      .build();
  }
}
