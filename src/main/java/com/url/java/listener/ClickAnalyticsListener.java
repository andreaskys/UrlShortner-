package com.url.java.listener;

import com.url.java.config.RabbitConfig;
import com.url.java.dto.ClickEventDTO;
import com.url.java.repository.UrlRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClickAnalyticsListener {

    private final UrlRepository repository;

    public ClickAnalyticsListener(UrlRepository repository){
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void processClick(ClickEventDTO event){
        System.out.println("Recebido evento de click: " + event);

        repository.incrementClicks(event.shortCode(), 1L);

        try{
            Thread.sleep(100);
            System.out.println("Dados processados para: " + event.shortCode());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
