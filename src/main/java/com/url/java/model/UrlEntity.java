package com.url.java.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "urls")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UrlEntity {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(nullable = false)
 private String originalUrl;

 @Column(nullable = false, unique = true)
 private String shortCode;

 @Column(nullable = false)
 private Long clickCount = 0L;

 private LocalDateTime createdAt = LocalDateTime.now();

 


}
