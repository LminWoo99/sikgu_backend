package com.example.plantsnsservice.service;

import com.example.plantsnsservice.dto.request.NotificationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationSender {
    private final KafkaTemplate<String, NotificationEventDto> kafkaTemplate;

    public void send(String topic, NotificationEventDto notificationEventDto) {
        kafkaTemplate.send(topic, notificationEventDto);
    }
}
