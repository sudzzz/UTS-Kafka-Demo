package com.example.uts.config;

import com.example.uts.constants.CommonConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    //spring bean for kafka topic

    @Bean
    public NewTopic topic(){
        return TopicBuilder.name(CommonConstants.BOOKING_PRODUCTION_SERVER).build();
    }
}
