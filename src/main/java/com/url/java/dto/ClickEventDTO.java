package com.url.java.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ClickEventDTO(
        String shortCode,
        String ipAddress,
        String userAgent,
        LocalDateTime timestamp
) implements Serializable { }
